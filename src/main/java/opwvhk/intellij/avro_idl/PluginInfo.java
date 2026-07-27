package opwvhk.intellij.avro_idl;

public record PluginInfo(String name, String version, String changeNotes) {
	private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

	String nonSnapshotVersion() {
		if (version.endsWith(SNAPSHOT_SUFFIX)) {
			return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length());
		}
		return version;
	}
}
