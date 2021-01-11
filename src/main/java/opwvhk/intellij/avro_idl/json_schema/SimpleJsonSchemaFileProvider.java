package opwvhk.intellij.avro_idl.json_schema;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider;
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory;
import com.jetbrains.jsonSchema.extension.SchemaType;
import com.jetbrains.jsonSchema.impl.JsonSchemaVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleJsonSchemaFileProvider implements JsonSchemaFileProvider {
	private final String fileTypeName;
	private final Class<? extends FileType> fileTypeType;
	private final String resourcePath;
	private final NullableLazyValue<VirtualFile> jsonSchemaFile;

	SimpleJsonSchemaFileProvider(@NotNull FileType fileType, @NotNull String resourcePath) {
		this.fileTypeName = fileType.getName();
		this.fileTypeType = fileType.getClass();
		this.resourcePath = resourcePath;
		this.jsonSchemaFile = NullableLazyValue.createValue(() ->
			JsonSchemaProviderFactory.getResourceFile(SimpleJsonSchemaFileProvider.class, this.resourcePath)
		);
	}

	@Override
	public boolean isAvailable(@NotNull VirtualFile file) {
		return fileTypeType.isInstance(file.getFileType());
	}

	@Override
	public @NotNull String getName() {
		return fileTypeName;
	}

	@Override
	public @Nullable VirtualFile getSchemaFile() {
		return jsonSchemaFile.getValue();
	}

	@Override
	public JsonSchemaVersion getSchemaVersion() {
		return JsonSchemaVersion.SCHEMA_7;
	}

	@Override
	public @NotNull SchemaType getSchemaType() {
		return SchemaType.embeddedSchema;
	}
}
