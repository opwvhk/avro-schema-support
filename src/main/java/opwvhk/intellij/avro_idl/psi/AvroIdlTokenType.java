package opwvhk.intellij.avro_idl.psi;

import com.intellij.psi.tree.IElementType;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;

public class AvroIdlTokenType extends IElementType {
	public AvroIdlTokenType(String debugName) {
		super(debugName, AvroIdlLanguage.INSTANCE);
	}

	@Override
	public String toString() {
		return "AvroIdlTokenType." + super.toString();
	}
}
