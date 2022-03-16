package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvroIdlMisplacedDocumentationInspection extends BaseAvroIdlInspection<PsiComment> {

	public AvroIdlMisplacedDocumentationInspection() {
		super(PsiComment.class);
	}

	/**
	 *
	 */
	@Override
	protected void visitElement(@NotNull PsiComment element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (element.getTokenType() != AvroIdlTypes.DOC_COMMENT) {
			// Not a documentation comment.
			return;
		}
		assert element.getFirstChild() == null;

		// Documentation is valid if the next visible non-comment leaf is part of a declaration (this means is cannot be whitespace, nor a comment),
		// and the previous non-comment leaf of the declaration is our (leaf) element (this means the next leaf was the first leaf of the declaration).
		PsiElement nextLeaf = AvroIdlPsiUtil.nextNonCommentLeaf(element);
		PsiElement declaration = PsiTreeUtil.findFirstParent(nextLeaf, this::isDeclaration);
		boolean validDocumentation = declaration != null && AvroIdlPsiUtil.prevNonCommentLeaf(declaration) == element;

		if (!validDocumentation) {
			holder.registerProblem(element, "Misplaced documentation comment: documentation comments should be placed directly before declarations",
				new RemoveDocumentationQuickFix(element, "Delete misplaced documentation comment"),
				new MakeCommentQuickFix(element, "Replace with multiline comment")
			);
		}
	}

	private boolean isDeclaration(PsiElement element) {
		return element instanceof AvroIdlProtocolDeclaration || element instanceof AvroIdlNamedSchemaDeclaration ||
			element instanceof AvroIdlFieldDeclaration || element instanceof AvroIdlVariableDeclarator ||
			element instanceof AvroIdlMessageDeclaration || element instanceof AvroIdlFormalParameter;
	}

	protected static class RemoveDocumentationQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<PsiComment> {
		public RemoveDocumentationQuickFix(@NotNull PsiComment element, String errorMessage) {
			super(element, errorMessage);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor, @NotNull PsiComment element) {
			element.delete();
		}
	}

	protected static class MakeCommentQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<PsiComment> {
		public MakeCommentQuickFix(@NotNull PsiComment element, String errorMessage) {
			super(element, errorMessage);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
		                      @NotNull PsiComment element) {
			final AvroIdlElementFactory elementFactory = new AvroIdlElementFactory(project);
			final PsiComment multilineComment = elementFactory.createMultilineComment(element.getText());
			element.replace(multilineComment);
		}
	}
}
