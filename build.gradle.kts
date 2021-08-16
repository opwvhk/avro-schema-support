import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import java.util.*

plugins {
	`java-library`
	id("java")
	id("org.jetbrains.intellij") version "1.1.4"
}

group = "net.sf.opk"
version = "203.0.3"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.apache.avro", "avro-compiler", "1.10.2").exclude("org.slf4j")
	testImplementation("junit", "junit", "4.13")
	testImplementation("org.assertj", "assertj-core", "3.20.2")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	version.set("2020.3")
	plugins.set(listOf("com.intellij.java", "PsiViewer:203-SNAPSHOT")) // Add the java plugin here to satisfy test dependencies.
}

tasks {
	patchPluginXml {
		version.set(project.version.toString())
		sinceBuild.set("203")
		untilBuild.set("212.*")
		changeNotes.set(
			"""
			<p>Version 203.0.3:</p>
			<ul>
			<li>Added startup check for incompatibilities (i.e. is the other Avro plugin active?)</li>
			</ul>
			<p>Version 203.0.2:</p>
			<ul>
			<li>IDL parser now also allows annotations before doc comments (issue #12).</li>
			</ul>
			<p>Version 203.0.1:</p>
			<ul>
			<li>Fixed NPEs upon file traversal for plugin actions (issue #9, #11).</li>
			<li>Renamed defined languages to prevant name clashes.</li>
			</ul>
			<p>Version 203.0.0:</p>
			<ul>
			<li>Changed version number to match IntelliJ builds</li>
			<li>Migrated away from deprecated API: minimum supported version is now 2020.3</li>
			<li>Added refactoring actions to convert Avro IDL to and from Avro schemas and Avro protocols.</li>
			<li>Added file icon variant for dark mode</li>
			</ul>
			<p>Version 0.2.1:</p>
			<ul>
			<li>Add Avro Schema and Avro Protocol languages, allowing language injection</li>
			</ul>
			<p>Version 0.2.0:</p>
			<ul>
			<li>Add support for imports</li>
			<li>Add file types and JSON schemas for <code>.avsc</code> &amp; <code>.avpr</code></li>
			</ul>
			<p>Version 0.1.1:</p>
			<ul>
			<li>Extra build for IntelliJ 2020.3</li>
			</ul>
			<p>Version 0.1.0:</p>
	        <ul>
			<li>Initial release</li>
			<li>Full parsing of Avro .avdl files, based on Avro 1.10 syntax</li>
			<li>Syntax highlighting & formatting</li>
			<li>Code completion based on syntax and supported references</li>
			<li>Some semantic checks</li>
			<li>Some refactoring support (renaming & deleting named types)</li>
	        </ul>
			"""
		)
	}

	runPluginVerifier {
		fun forIdes(vararg ides: String): (String) -> List<String> { return { version -> ides.map { "$it-$version" } } }
		// IntelliJ Community, IntelliJ Ultimate, PyCharm Community &, PYcharm professional editions with versions ranging from Fall 2020 to the latest release
		val intelijVersions = listOf("2020.3.2", "2021.1", "2021.1.2", "2021.2").flatMap(forIdes("IC", "IU"))
		val pycharmVersions = listOf("2020.3.3", "2021.1", "2021.1.2", "2021.2").flatMap(forIdes("PCC", "PY"))
		ideVersions.set(intelijVersions + pycharmVersions)
		failureLevel.set(EnumSet.complementOf(EnumSet.of(FailureLevel.DEPRECATED_API_USAGES)))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")
