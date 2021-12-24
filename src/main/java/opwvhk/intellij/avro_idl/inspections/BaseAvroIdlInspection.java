package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseAvroIdlInspection<T extends PsiElement> extends LocalInspectionTool {

	private final Class<T> elementType;

	public BaseAvroIdlInspection(Class<T> elementType) {
		this.elementType = elementType;
	}

	protected abstract void visitElement(@NotNull T element, @NotNull ProblemsHolder holder, @NotNull LocalInspectionToolSession session);

	@Override
	public @Nullable @Nls String loadDescription() {
		return super.loadDescription();
	}

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
				if (elementType.isAssignableFrom(element.getClass())) {
					BaseAvroIdlInspection.this.visitElement(elementType.cast(element), holder, session);
				}
			}
		};
	}
	protected LocalQuickFix createQuickFix(@NotNull T element, @IntentionName @NotNull String text, @NotNull QuickFixAction<T> quickFixAction) {
		return new LocalQuickFixOnPsiElement(element) {
			@Override
			public @IntentionFamilyName @NotNull String getFamilyName() {
				return "Avro IDL";
			}

			@Override
			public @IntentionName @NotNull String getText() {
				return text;
			}

			@Override
			public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
				quickFixAction.invoke(project, file, element); // element, startElement and endElement are all the same instance; this one doesn't need a cast
			}
		};
	}

	@FunctionalInterface
	protected interface QuickFixAction<E> {
		void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull E element);
	}
}
