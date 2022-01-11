package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalQuickFixOnPsiElement;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleAvroIdlQuickFixOnPsiElement<E extends PsiElement> extends LocalQuickFixOnPsiElement {

	private final String text;

	public SimpleAvroIdlQuickFixOnPsiElement(@NotNull E element, @IntentionName @NotNull String text) {
		super(element);
		this.text = text;
	}

	protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull E element) {
		return true;
	}

	protected abstract void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull E element);

	@Override
	public @IntentionFamilyName @NotNull String getFamilyName() {
		return "Avro IDL";
	}

	@Override
	public @IntentionName @NotNull String getText() {
		return text;
	}

	@Override
	public boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		//noinspection unchecked
		return isAvailable(project, file, (E)startElement);
	}

	@Override
	public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		//noinspection unchecked
		invoke(project, file, (E)startElement);
	}
}
