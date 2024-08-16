package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlEnumDeclaration;

public class AvroIdlEnumNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlEnumNamingConvention() {
		super(AvroIdlEnumDeclaration.class, 3, 32, IDENTIFIER_START_UPPERCASE);
	}

	@Override
	public String getElementDescription() {
		return TextBundle.message("naming.enum");
	}
}
