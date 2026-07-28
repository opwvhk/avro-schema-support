package opwvhk.intellij.avro_idl;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class AvroIdlPluginUtils {
	private static final String PLUGIN_XML_PATH = "/META-INF/plugin.xml";

	@NotNull
	public static PluginInfo getMyPluginInfo() {
		try (InputStream pluginXmlStream = AvroIdlPluginUtils.class.getResourceAsStream(PLUGIN_XML_PATH)) {
			String pluginXml = new String(pluginXmlStream.readAllBytes());
			// Extract parts from plugin XML.
			// Uses the fact that
			String name = findMatchBetween(pluginXml, "<name>", "</name>");
			String version = findMatchBetween(pluginXml, "<version>", "</version>");
			String changes = findMatchBetween(pluginXml, "<change-notes><![CDATA[", "]]></change-notes>");

			return new PluginInfo(name, version, changes);
		} catch (IOException e) {
			throw new NullPointerException("Own description is null: broken plugin!");
		}
	}

	private static String findMatchBetween(String text, String prefix, String suffix) {
		int prefixPos = text.indexOf(prefix);
		int suffixPos = text.indexOf(suffix, prefixPos + 1);

		if (prefixPos == -1 || suffixPos == -1) {
			throw new IllegalArgumentException("No text found between " + prefix + " and " + suffix + "in plugin XML");
		}

		assert (text.indexOf(prefix, prefixPos + 1) == -1) : "Multiple occurrences of " + prefix + " found in plugin XML";
		assert (text.indexOf(suffix, suffixPos + 1) == -1) : "Multiple occurrences of " + suffix + " found in plugin XML";

		return text.substring(prefixPos + prefix.length(), suffixPos);
	}
}
