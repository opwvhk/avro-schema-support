package opwvhk.intellij.avro_idl.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.editorActions.TypedHandlerUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import opwvhk.intellij.avro_idl.psi.AvroIdlFile;
import opwvhk.intellij.avro_idl.psi.AvroIdlTypes;
import org.jetbrains.annotations.NotNull;

public class AvroIdlAngleBracketTypedHandler extends TypedHandlerDelegate {

	private static final @NotNull TokenSet INVALID_INSIDE_ANGLE = TokenSet.create(AvroIdlTypes.SEMICOLON);

	@Override
	public @NotNull Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
		if (c == '<' && file instanceof AvroIdlFile) {
			TypedHandlerUtil.handleAfterGenericLT(editor, AvroIdlTypes.LEFT_ANGLE, AvroIdlTypes.RIGHT_ANGLE, INVALID_INSIDE_ANGLE);
			return Result.STOP;
		}

		return Result.CONTINUE;
	}
}
