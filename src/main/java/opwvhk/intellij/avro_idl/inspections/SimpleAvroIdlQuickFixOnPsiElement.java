package opwvhk.intellij.avro_idl.inspections;

import com.intellij.codeInsight.intention.preview.IntentionPreviewUtils;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleAvroIdlQuickFixOnPsiElement<E extends PsiElement>
		extends LocalQuickFixAndIntentionActionOnPsiElement {

	private final String text;

	public SimpleAvroIdlQuickFixOnPsiElement(@NotNull E element, @IntentionName @NotNull String text) {
		super(element);
		this.text = text;
	}

	protected boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull E element) {
		return true;
	}

	protected abstract void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
	                               @NotNull E element);

	@Override
	@NotNull
	public @IntentionFamilyName String getFamilyName() {
		return "Avro IDL";
	}

	@Override
	@NotNull
	public @IntentionName String getText() {
		return text;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isAvailable(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement,
	                           @NotNull PsiElement endElement) {
		return isAvailable(project, file, (E) startElement);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement,
	                   @NotNull PsiElement endElement) {
		CommandProcessor.getInstance().executeCommand(project,
				() -> invoke(project, file, (Editor) null, (E) startElement), text, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
	                   @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
		CommandProcessor.getInstance().executeCommand(project,
				() -> invoke(project, file, editor, (E) startElement), text, null);
	}

	protected void selectElement(Editor editor, @NotNull PsiElement element) {
		if (editor != null && !IntentionPreviewUtils.isIntentionPreviewActive()) {
			TextRange range = element.getTextRange();
			// Remove all carets but the "main"
			LogicalPosition typeStartPosition = editor.offsetToLogicalPosition(range.getStartOffset());
			LogicalPosition typeEndPosition = editor.offsetToLogicalPosition(range.getEndOffset());
			editor.getCaretModel().setCaretsAndSelections(
					List.of(new CaretState(typeStartPosition, typeStartPosition, typeEndPosition)));
			editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
		}
	}
}
