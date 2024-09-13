package opwvhk.intellij.avro_idl.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static opwvhk.intellij.avro_idl.psi.AvroIdlTypes.*;

public class AvroIdlFoldingBuilder implements FoldingBuilder, DumbAware {
	@Override
	public @NotNull FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
		List<FoldingDescriptor> descriptors = new ArrayList<>();
		buildFoldRegions(node, document, descriptors);
		return descriptors.toArray(FoldingDescriptor[]::new);
	}

	private void buildFoldRegions(@NotNull ASTNode node, @NotNull Document document,
	                              @NotNull List<FoldingDescriptor> descriptors) {
		if (isSingleLine(node, document)) {
			return;
		}

		final TextRange foldRange = getFoldRange(node);
		if (foldRange != null) {
			descriptors.add(new FoldingDescriptor(node, foldRange));
		}

		for (ASTNode child : node.getChildren(null)) {
			buildFoldRegions(child, document, descriptors);
		}
	}

	@Override
	@Nullable
	public String getPlaceholderText(@NotNull ASTNode node) {
		final IElementType elementType = node.getElementType();
		if (elementType == BLOCK_COMMENT) {
			return "/*...*/";
		} else if (elementType == DOC_COMMENT) {
			return "/**...*/";
		} else if (elementType == JSON_ARRAY) {
			return "[...]";
		} else {
			return "{...}";
		}
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node) {
		final IElementType elementType = node.getElementType();
		return elementType == RECORD_BODY || elementType == ENUM_BODY;
	}

	private boolean isSingleLine(@NotNull ASTNode node, @NotNull Document document) {
		final TextRange range = node.getTextRange();
		return document.getLineNumber(range.getStartOffset()) == document.getLineNumber(range.getEndOffset());
	}

	@Nullable
	private TextRange getFoldRange(@NotNull ASTNode node) {
		final IElementType elementType = node.getElementType();
		if (elementType == BLOCK_COMMENT || elementType == DOC_COMMENT) {
			return node.getTextRange();
		}

		if (elementType == PROTOCOL_DECLARATION || elementType == RECORD_DECLARATION ||
				elementType == ENUM_DECLARATION || elementType == JSON_OBJECT) {
			return getFoldRange(node, LEFT_BRACE, RIGHT_BRACE);
		} else if (elementType == JSON_ARRAY) {
			return getFoldRange(node, LEFT_BRACKET, RIGHT_BRACKET);
		}

		return null;
	}

	@Nullable
	private TextRange getFoldRange(@NotNull ASTNode node, @NotNull IElementType startElementType,
	                               @NotNull IElementType endElementType) {
		final ASTNode foldStart = node.findChildByType(startElementType);
		final ASTNode foldEnd = node.findChildByType(endElementType, foldStart);
		if (foldStart != null && foldEnd != null) {
			return new TextRange(foldStart.getStartOffset(), foldEnd.getTextRange().getEndOffset());
		}
		return null;
	}
}
