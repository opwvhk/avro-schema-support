package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.psi.AvroIdlJsonStringLiteral;
import opwvhk.intellij.avro_idl.psi.AvroIdlNameIdentifierOwner;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamespaceProperty;
import opwvhk.intellij.avro_idl.psi.AvroIdlSchemaProperty;

public class AvroIdlNamespaceNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlNamespaceNamingConvention() {
		super(AvroIdlNamespaceProperty.class, 3, 32, "[a-z][a-z0-9_]*[a-z0-9](\\.[a-z][a-z0-9_]*[a-z0-9])*");
	}

	@Override
	public String getElementDescription() {
		return "Namespace";
	}
}
