package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlFixedDeclaration;

public class AvroIdlFixedNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlFixedNamingConvention() {
		super(AvroIdlFixedDeclaration.class, 3, 32, IDENTIFIER_START_UPPERCASE);
	}

	@Override
	public String getElementDescription() {
		return TextBundle.message("naming.fixed");
	}
}
