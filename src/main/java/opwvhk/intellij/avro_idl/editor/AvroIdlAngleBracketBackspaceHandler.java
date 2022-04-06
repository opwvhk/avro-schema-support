package opwvhk.intellij.avro_idl.editor;

import com.intellij.psi.PsiFile;

public class AvroIdlAngleBracketBackspaceHandler extends AbstractBackspaceHandler {
	public AvroIdlAngleBracketBackspaceHandler() {
		super('<', '>');
	}

	@Override
	boolean shouldBeDeleted(PsiFile file, int offset) {
		return true;
	}
}
