package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.psi.AvroIdlVariableDeclarator;

public class AvroIdlFieldNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlFieldNamingConvention() {
		super(AvroIdlVariableDeclarator.class, 3, 32, IDENTIFIER_START_LOWERCASE);
	}

	@Override
	public String getElementDescription() {
		return "Fixed";
	}
}
