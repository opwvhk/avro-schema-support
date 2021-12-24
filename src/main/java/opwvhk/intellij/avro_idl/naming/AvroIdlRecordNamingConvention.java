package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlRecordDeclaration;

public class AvroIdlRecordNamingConvention extends AvroIdlNamingConvention {

	public AvroIdlRecordNamingConvention() {
		super(AvroIdlRecordDeclaration.class, 3, 32, IDENTIFIER_START_UPPERCASE);
	}

	@Override
	public String getElementDescription() {
		return "Record";
	}

	@Override
	public boolean isApplicable(AvroIdlNameIdentifierOwner member) {
		// The shortcut operator prevents ClassCastExceptions
		return super.isApplicable(member) && ((AvroIdlRecordDeclaration)member).isErrorType();
	}
}
