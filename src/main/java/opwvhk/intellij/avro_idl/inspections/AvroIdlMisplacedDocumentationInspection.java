package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlDocumentation;
import opwvhk.intellij.avro_idl.psi.AvroIdlElementFactory;
import opwvhk.intellij.avro_idl.psi.AvroIdlMisplacedDocumentation;
import org.jetbrains.annotations.NotNull;

public class AvroIdlMisplacedDocumentationInspection extends BaseAvroIdlInspection<AvroIdlDocumentation> {
	public AvroIdlMisplacedDocumentationInspection() {
		super(AvroIdlDocumentation.class);
	}

	@Override
	protected void visitElement(@NotNull AvroIdlDocumentation element,
	                            @NotNull ProblemsHolder holder,
	                            @NotNull LocalInspectionToolSession session) {
		if (element instanceof AvroIdlMisplacedDocumentation || PsiTreeUtil.skipWhitespacesForward(element) instanceof AvroIdlDocumentation) {
			holder.registerProblem(element, "Misplaced documentation comment: documentation comments should be placed directly before declarations",
				new RemoveDocumentationQuickFix(element, "Delete misplaced documentation comment"),
				new MakeCommentQuickFix(element, "Replace with multiline comment")
			);
		}
	}

	protected static class RemoveDocumentationQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlDocumentation> {
		public RemoveDocumentationQuickFix(@NotNull AvroIdlDocumentation element, String errorMessage) {
			super(element, errorMessage);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull AvroIdlDocumentation element) {
			element.delete();
		}
	}

	protected static class MakeCommentQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlDocumentation> {
		public MakeCommentQuickFix(@NotNull AvroIdlDocumentation element, String errorMessage) {
			super(element, errorMessage);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull AvroIdlDocumentation element) {
			final AvroIdlElementFactory elementFactory = new AvroIdlElementFactory(project);
			final PsiComment multilineComment = elementFactory.createMultilineComment(element);
			element.replace(multilineComment);
		}
	}
}
