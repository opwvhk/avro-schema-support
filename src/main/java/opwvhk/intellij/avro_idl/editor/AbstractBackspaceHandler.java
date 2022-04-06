package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBackspaceHandler extends BackspaceHandlerDelegate {

	private final char expectedDeletedChar;
	private final char expectedNextChar;

	public AbstractBackspaceHandler(char expectedDeletedChar, char expectedNextChar) {
		this.expectedDeletedChar = expectedDeletedChar;
		this.expectedNextChar = expectedNextChar;
	}

	@Override
	public void beforeCharDeleted(char deletingChar, @NotNull PsiFile file, @NotNull Editor editor) {
		// do nothing
	}

	@Override
	public boolean charDeleted(char deletedChar, @NotNull PsiFile file, @NotNull Editor editor) {
		if (deletedChar == expectedDeletedChar && file instanceof AvroIdlFile) {
			var offset = editor.getCaretModel().getOffset();
			if (offset < 1 || offset >= editor.getDocument().getTextLength()) {
				return false;
			}
			var document = editor.getDocument();
			var nextChar = document.getCharsSequence().charAt(offset);
			if (nextChar == expectedNextChar && shouldBeDeleted(file, offset)) {
				document.replaceString(offset, offset + 1, "");
				return true;
			}
		}
		return false;
	}

	abstract boolean shouldBeDeleted(PsiFile file, int offset);

}
