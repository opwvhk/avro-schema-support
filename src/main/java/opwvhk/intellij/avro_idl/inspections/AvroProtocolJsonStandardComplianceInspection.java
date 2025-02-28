package opwvhk.intellij.avro_idl.inspections;

import opwvhk.intellij.avro_idl.AvroProtocolLanguage;

public class AvroProtocolJsonStandardComplianceInspection extends AvroSchemaJsonStandardComplianceInspection {
	public AvroProtocolJsonStandardComplianceInspection() {
		super(AvroProtocolLanguage.INSTANCE);
	}
}
