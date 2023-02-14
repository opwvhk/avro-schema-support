package opwvhk.intellij.avro_idl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AvroFileTypesTest {
	@Test
	public void validateDefaultExtensions() {
		assertThat(AvroIdlFileType.INSTANCE.getDefaultExtension()).isEqualTo("avdl");
		assertThat(AvroProtocolFileType.INSTANCE.getDefaultExtension()).isEqualTo("avpr");
		assertThat(AvroSchemaFileType.INSTANCE.getDefaultExtension()).isEqualTo("avsc");
	}

	@Test
	public void validateIcons() {
		assertThat(AvroIdlFileType.INSTANCE.getIcon()).isSameAs(AvroIdlIcons.FILE);
		assertThat(AvroProtocolFileType.INSTANCE.getIcon()).isSameAs(AvroIdlIcons.FILE);
		assertThat(AvroSchemaFileType.INSTANCE.getIcon()).isSameAs(AvroIdlIcons.FILE);
	}
}
