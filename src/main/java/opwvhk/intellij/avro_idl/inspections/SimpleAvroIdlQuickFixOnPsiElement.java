package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleAvroIdlQuickFixOnPsiElement<E extends PsiElement> extends LocalQuickFixAndIntentionActionOnPsiElement {

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
	@SuppressWarnings("unchecked")
	public boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		return isAvailable(project, file, (E)startElement);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		invoke(project, file, (E)startElement);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
	                   @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		invoke(project, file, (E)startElement);
	}
}
