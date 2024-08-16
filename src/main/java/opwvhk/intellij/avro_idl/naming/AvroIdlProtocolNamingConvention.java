package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlProtocolDeclaration;

public class AvroIdlProtocolNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlProtocolNamingConvention() {
		super(AvroIdlProtocolDeclaration.class, 3, 32, IDENTIFIER_START_UPPERCASE);
	}

	@Override
	public String getElementDescription() {
		return TextBundle.message("naming.protocol");
	}
}
