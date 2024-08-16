package opwvhk.intellij.avro_idl.naming;

import opwvhk.intellij.avro_idl.TextBundle;
import opwvhk.intellij.avro_idl.psi.AvroIdlNamespaceIdentifierOwner;

public class AvroIdlNamespaceNamingConvention extends AvroIdlNamingConvention {
	public AvroIdlNamespaceNamingConvention() {
		super(AvroIdlNamespaceIdentifierOwner.class, 3, 32, "[a-z][a-z0-9_]*[a-z0-9](\\.[a-z][a-z0-9_]*[a-z0-9])*");
	}

	@Override
	public String getElementDescription() {
		return TextBundle.message("naming.namespace");
	}
}
