package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.psi.AvroIdlDocumentation;
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
		if (element instanceof AvroIdlMisplacedDocumentation) {
			holder.registerProblem(element, "Misplaced documentation comment: documentation comments should be placed before declarations",
				new RemoveDocumentationQuickFix(element, "Delete misplaced documentation comment"));
		} else if (PsiTreeUtil.skipWhitespacesForward(element) instanceof AvroIdlDocumentation) {
			// The grammar recognizes doc comment sequences to allow the distinction between dangling and misplaced comments.
			// As a result, misplaced comments can never occur after good or dangling doc comments.
			holder.registerProblem(element, "Dangling documentation comment: the next documentation comments causes this one to be ignored",
				new RemoveDocumentationQuickFix(element, "Delete dangling documentation comment"));
		}
	}

	private static class RemoveDocumentationQuickFix extends SimpleAvroIdlQuickFixOnPsiElement<AvroIdlDocumentation> {
		public RemoveDocumentationQuickFix(@NotNull AvroIdlDocumentation element, String errorMessage) {
			super(element, errorMessage);
		}

		@Override
		protected void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull AvroIdlDocumentation element) {
			element.delete();
		}
	}
}
