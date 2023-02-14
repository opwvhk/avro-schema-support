package opwvhk.intellij.avro_idl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.APP)
@State(name = "AvroIdlSettings", storages = @Storage(value = "avro_idl.xml", roamingType = RoamingType.DISABLED))
public final class AvroIdlSettings implements PersistentStateComponent<AvroIdlSettings> {

	private String pluginVersion;

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	@NotNull
	public static AvroIdlSettings getInstance() {
		return ApplicationManager.getApplication().getService(AvroIdlSettings.class);
	}

	@Override
	@NotNull
	public AvroIdlSettings getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull AvroIdlSettings state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
