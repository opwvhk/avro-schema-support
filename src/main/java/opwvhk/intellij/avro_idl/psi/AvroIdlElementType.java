package opwvhk.intellij.avro_idl.psi;

import com.intellij.psi.tree.IElementType;
import opwvhk.intellij.avro_idl.AvroIdlLanguage;

public class AvroIdlElementType extends IElementType {
	public AvroIdlElementType(String debugName) {
		super(debugName, AvroIdlLanguage.INSTANCE);
	}
}
