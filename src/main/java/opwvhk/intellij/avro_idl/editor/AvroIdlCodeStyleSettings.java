package opwvhk.intellij.avro_idl.editor;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

public class AvroIdlCodeStyleSettings extends CustomCodeStyleSettings {
	public AvroIdlCodeStyleSettings(CodeStyleSettings container) {
		super("AvroIdlCodeStyleSettings", container);
	}

	public boolean ADD_ALIAS_ON_SCHEMA_RENAME = false;
	public boolean ADD_ALIAS_ON_FIELD_RENAME = true;
}
