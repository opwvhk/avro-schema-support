package opwvhk.intellij.avro_idl.editor;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.Nullable;

public class AvroIdlCommenter implements CodeDocumentationAwareCommenter {
	@Override
	public @Nullable IElementType getLineCommentTokenType() {
		return AvroIdlTypes.LINE_COMMENT;
	}

    @Override
    public @Nullable String getLineCommentPrefix() {
        return "//";
    }

	@Override
	public @Nullable IElementType getBlockCommentTokenType() {
		return AvroIdlTypes.BLOCK_COMMENT;
	}

    @Override
    public @Nullable String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    public @Nullable String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    public @Nullable String getCommentedBlockCommentPrefix() {
        return null;
    }

    @Override
    public @Nullable String getCommentedBlockCommentSuffix() {
        return null;
    }

	@Override
	public @Nullable IElementType getDocumentationCommentTokenType() {
		return AvroIdlTypes.DOC_COMMENT;
	}

	@Override
	public @Nullable String getDocumentationCommentPrefix() {
		return "/**";
	}

	@Override
	public @Nullable String getDocumentationCommentLinePrefix() {
		return " * ";
	}

	@Override
	public @Nullable String getDocumentationCommentSuffix() {
		return " */";
	}

	@Override
	public boolean isDocumentationComment(PsiComment element) {
		return element.getTokenType() == AvroIdlTypes.DOC_COMMENT;
	}
}
