package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseAvroIdlInspectionTool extends LocalInspectionTool {

	protected abstract void visitElement(@NotNull PsiElement element, @NotNull ProblemsHolder holder, @NotNull LocalInspectionToolSession session);

	@Override
	public @Nullable String getDescriptionFileName() {
		String className = getClass().getCanonicalName();
		if (className == null) {
			// Should not happen!
			return null;
		}
		return className + ".html";
	}

	@Override
	public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly,
												   @NotNull LocalInspectionToolSession session) {
		return new PsiElementVisitor() {
			@Override
			public void visitElement(@NotNull PsiElement element) {
				BaseAvroIdlInspectionTool.this.visitElement(element, holder, session);
			}
		};
	}
}
