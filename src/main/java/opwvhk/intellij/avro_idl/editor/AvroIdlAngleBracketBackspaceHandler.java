package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import org.jetbrains.annotations.NotNull;

public class AvroIdlAngleBracketBackspaceHandler extends BackspaceHandlerDelegate {
	@Override
	public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
		// Nothing to do.
	}

	@Override
	public boolean charDeleted(char deletedChar, @NotNull PsiFile file, @NotNull Editor editor) {
		if (!CodeInsightSettings.getInstance().AUTOINSERT_PAIR_BRACKET) return false;

		if (deletedChar == '<' && file instanceof AvroIdlFile) {
			var offset = editor.getCaretModel().getOffset();
			if (offset < 1 || offset >= editor.getDocument().getTextLength()) {
				return false;
			}
			var document = editor.getDocument();
			var nextChar = document.getCharsSequence().charAt(offset);
			if (nextChar == '>') {
				document.replaceString(offset, offset + 1, "");
				return true;
			}
		}
		return false;
	}
}
