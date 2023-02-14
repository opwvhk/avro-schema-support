package opwvhk.intellij.avro_idl.language;

import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import opwvhk.intellij.avro_idl.inspections.SimpleAvroIdlQuickFixOnPsiElement;
import opwvhk.intellij.avro_idl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AddDeclarationFix extends SimpleAvroIdlQuickFixOnPsiElement<PsiElement> {
	protected final String template;

	/**
	 * <p>Create an "Add declaration" fix.</p>
	 *
	 * <p>Constraints on the template:</p>
	 * <ol><li>
	 *     Only the first non-whitespace element (and its children) will be used.
	 * </li><li>
	 *     There must be a comment in the element: the first comment will be removed, and the cursor will be placed there.
	 * </li></ol>
	 *
	 * @param element  a schema/message declaration or one of its children; the new declaration will be added after the declaration
	 * @param name     the name of the fix
	 * @param template a template for the declaration, subject to the constraints above
	 */
	public AddDeclarationFix(@NotNull PsiElement element, @IntentionName @NotNull String name, String template) {
		super(element, name);
		this.template = template;
	}

	@Override
	protected void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable Editor editor,
	                      @NotNull PsiElement element) {
		element = PsiTreeUtil.findFirstParent(element,
				e -> e instanceof AvroIdlNamedSchemaDeclaration || e instanceof AvroIdlMainSchemaDeclaration ||
						e instanceof AvroIdlMessageDeclaration);
		if (element == null) {
			return;
		}

		// Create a file with the content we want to add. Note: only the first non-whitespace element will be used!
		AvroIdlElementFactory factory = new AvroIdlElementFactory(project);
		AvroIdlFile dummyFile = factory.createDummyFile(template);
		PsiElement newElement = dummyFile.getFirstChild();
		while (newElement instanceof PsiWhiteSpace) {
			newElement = newElement.getNextSibling();
		}

		PsiElement addedElement = element.getParent().addAfter(newElement, element);
		PsiElement focusElement = findFirstComment(addedElement);
		int cursorOffset = requireNonNull(focusElement, "Programming error: missing element to focus").getTextOffset();
		focusElement.delete();

		if (editor != null) {
			// Replace all carets with a single one
			LogicalPosition cursorPosition = editor.offsetToLogicalPosition(cursorOffset);
			editor.getCaretModel().setCaretsAndSelections(List.of(new CaretState(cursorPosition, null, null)));
			editor.getScrollingModel().scrollTo(cursorPosition, ScrollType.MAKE_VISIBLE);
		}
	}

	@Nullable
	private PsiElement findFirstComment(@NotNull PsiElement element) {
		final PsiElement firstLeaf = PsiTreeUtil.getDeepestFirst(element);
		for (PsiElement e = firstLeaf; e != null; e = PsiTreeUtil.nextLeaf(e)) {
			if (e instanceof PsiComment) {
				return e;
			}
		}
		return null;
	}

	@Override
	public boolean availableInBatchMode() {
		return false;
	}
}
