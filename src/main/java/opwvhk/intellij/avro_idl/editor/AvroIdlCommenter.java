package opwvhk.intellij.avro_idl.editor;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.Nullable;

public class AvroIdlCommenter implements CodeDocumentationAwareCommenter {
	@Override
	@Nullable
	public IElementType getLineCommentTokenType() {
		return AvroIdlTypes.LINE_COMMENT;
	}

	@Override
	@Nullable
	public String getLineCommentPrefix() {
		return "//";
	}

	@Override
	@Nullable
	public IElementType getBlockCommentTokenType() {
		return AvroIdlTypes.BLOCK_COMMENT;
	}

	@Override
	@Nullable
	public String getBlockCommentPrefix() {
		return "/*";
	}

	@Override
	@Nullable
	public String getBlockCommentSuffix() {
		return "*/";
	}

	@Override
	@Nullable
	public String getCommentedBlockCommentPrefix() {
		return null;
	}

	@Override
	@Nullable
	public String getCommentedBlockCommentSuffix() {
		return null;
	}

	@Override
	@Nullable
	public IElementType getDocumentationCommentTokenType() {
		return AvroIdlTypes.DOC_COMMENT;
	}

	@Override
	@Nullable
	public String getDocumentationCommentPrefix() {
		return "/**";
	}

	@Override
	@Nullable
	public String getDocumentationCommentLinePrefix() {
		return " * ";
	}

	@Override
	@Nullable
	public String getDocumentationCommentSuffix() {
		return " */";
	}

	@Override
	public boolean isDocumentationComment(PsiComment element) {
		return element.getTokenType() == AvroIdlTypes.DOC_COMMENT;
	}
}
