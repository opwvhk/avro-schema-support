package opwvhk.intellij.avro_idl.json_schema;

import com.intellij.openapi.project.Project;
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider;
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory;
import opwvhk.intellij.avro_idl.AvroProtocolFileType;
import opwvhk.intellij.avro_idl.AvroSchemaFileType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AvroJsonSchemaProviderFactory implements JsonSchemaProviderFactory {
	@Override
    @NotNull
    public List<JsonSchemaFileProvider> getProviders(@NotNull Project project) {
		if (project.isDisposed()) {
			return Collections.emptyList();
		}
		return Arrays.asList(
			new SimpleJsonSchemaFileProvider(AvroSchemaFileType.INSTANCE, "/schemas/avro-schema.schema.json"),
			new SimpleJsonSchemaFileProvider(AvroProtocolFileType.INSTANCE, "/schemas/avro-protocol.schema.json")
		);
	}
}
