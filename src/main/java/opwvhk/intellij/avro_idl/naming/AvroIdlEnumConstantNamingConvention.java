package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumConstant;

public class AvroIdlEnumConstantNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlEnumConstantNamingConvention() {
		super(AvroIdlEnumConstant.class, 3, 32, "[A-Z][A-Z0-9_]*[A-Z0-9]");
	}

	@Override
	public String getElementDescription() {
		return TextBundle.message("naming.enum.constant");
	}
}
