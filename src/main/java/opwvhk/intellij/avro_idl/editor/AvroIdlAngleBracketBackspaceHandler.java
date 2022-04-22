package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.codeInsight.editorActions.TypedHandlerUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.NotNull;

import static opwvhk.intellij.avro_idl.editor.AvroIdlAngleBracketTypedHandler.INVALID_INSIDE_ANGLE;

public class AvroIdlAngleBracketBackspaceHandler extends BackspaceHandlerDelegate {
	@Override
	public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
		// Nothing to do.
	}

	@Override
	public boolean charDeleted(char deletedChar, @NotNull PsiFile file, @NotNull Editor editor) {
		if (!CodeInsightSettings.getInstance().AUTOINSERT_PAIR_BRACKET) return false;
		if (!(file instanceof AvroIdlFile)) return false;

		if (deletedChar == '<') {
			var offset = editor.getCaretModel().getOffset();
			if (offset < 1 || offset >= editor.getDocument().getTextLength()) {
				return false;
			}
			TypedHandlerUtil.handleGenericLTDeletion(editor, offset, AvroIdlTypes.LEFT_ANGLE, AvroIdlTypes.RIGHT_ANGLE, INVALID_INSIDE_ANGLE);
			return true;
			//var document = editor.getDocument();
			//var nextChar = document.getCharsSequence().charAt(offset);
			//if (nextChar == '>') {
			//	document.replaceString(offset, offset + 1, "");
			//	return true;
			//}
		}
		return false;
	}
}
