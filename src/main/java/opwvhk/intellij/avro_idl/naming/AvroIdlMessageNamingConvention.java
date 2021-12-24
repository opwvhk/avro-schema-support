package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.psi.AvroIdlMessageDeclaration;

public class AvroIdlMessageNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlMessageNamingConvention() {
		super(AvroIdlMessageDeclaration.class, 3, 32, IDENTIFIER_START_LOWERCASE);
	}

	@Override
	public String getElementDescription() {
		return "Message";
	}
}
