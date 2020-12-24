package opwvhk.intellij.avro_idl.psi;

import opwvhk.intellij.avro_idl.AvroIdlLanguage;
import com.intellij.psi.tree.IElementType;

public class AvroIdlElementType extends IElementType {
    public AvroIdlElementType(String debugName) {
        super(debugName, AvroIdlLanguage.INSTANCE);
    }
}
