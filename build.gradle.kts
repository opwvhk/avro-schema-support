import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import java.util.*

plugins {
	`java-library`
	id("java")
	id("org.jetbrains.intellij") version "1.3.0"
}

group = "net.sf.opk"
version = "213.0.0-SNAPSHOT"

repositories {
	mavenCentral()
}
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(11))
	}
}

dependencies {
	implementation("org.apache.avro", "avro-compiler", "1.11.0").exclude("org.slf4j")
	testImplementation("junit", "junit", "4.13")
	testImplementation("org.assertj", "assertj-core", "3.20.2")
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:unchecked")
	options.isDeprecation = true
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	//version.set("2020.3")
	version.set("2021.2.3")
	plugins.set(listOf("com.intellij.java", "PsiViewer:212-SNAPSHOT")) // Add the java plugin here to satisfy test dependencies.
}

tasks {
	patchPluginXml {
		version.set(project.version.toString())
		sinceBuild.set("203")
		untilBuild.set("221.*")
		changeNotes.set(
			"""
			<p>Version 212.0.0:</p>
			<ul data-version="212.0.0">
			<li>Refactoring actions (converting IDL from/to schema/protocol files) now use the build tool window</li>
			<li>Report changes upon startup after installation</li>
			<li>Miscellaneous refactoring and small cosmetic changes</li>
			<li>Converted some warnings to inspections</li>
			<li>Added inspection for naming conventions</li>
			<li>Add dialog for new IDL files</li>
			<li>Added support for Kotlin style nullable types (Avro &ge; 1.11.1)</li>
			<li>Updated documentation comments to conform to spec (as fixed in Avro &ge; 1.11.1)</li>
			</ul>
			<p>Version 203.1.2:</p>
			<ul data-version="203.1.2">
			<li>Fixed exception when writing a schema as IDL (issue #16)</li>
			<li>Improved formatting of message properties</li>
			<li>Improved file import references</li>
			<li>Fixed range exception in references</li>
			<li>Added error for annotations on references (triggers a bug in Avro &lt; 1.11.1, breaks in later Avro versions)</li>
			<li>The plugin is now tested with 2021.2.3 instead of 2020.3</li>
			<li>Improve identifier parsing to match the Avro IDL grammar</li>
			</ul>
			<p>Version 203.1.1:</p>
			<ul data-version="203.1.1">
			<li>Fixed NPE in import resolution (issue #14)</li>
			<li>Adjusted IDL parsing: improved detection of annotations for messages</li>
			<li>Adjusted IDL parsing: allows for more dangling doc comments as allowed by the official parser</li>
			</ul>
			<p>Version 203.1.0:</p>
			<ul data-version="203.1.0">
			<li>Adjusted IDL parsing to allow dangling doc comments</li>
			<li>Added warnings for dangling doc comments (the Avro IDL compiler ignores these)</li>
			</ul>
			<p>Version 203.0.3:</p>
			<ul data-version="203.0.3">
			<li>Added startup check for incompatibilities (i.e. is the other Avro plugin active?)</li>
			</ul>
			<p>Version 203.0.2:</p>
			<ul data-version="203.0.2">
			<li>IDL parser now also allows annotations before doc comments (issue #12).</li>
			</ul>
			<p>Version 203.0.1:</p>
			<ul data-version="203.0.1">
			<li>Fixed NPEs upon file traversal for plugin actions (issue #9, #11).</li>
			<li>Renamed defined languages to prevent name clashes.</li>
			</ul>
			<p>Version 203.0.0:</p>
			<ul data-version="203.0.0">
			<li>Changed version number to match IntelliJ builds</li>
			<li>Migrated away from deprecated API: minimum supported version is now 2020.3</li>
			<li>Added refactoring actions to convert Avro IDL to and from Avro schemas and Avro protocols.</li>
			<li>Added file icon variant for dark mode</li>
			</ul>
			<p>Version 0.2.1:</p>
			<ul data-version="0.2.1">
			<li>Add Avro Schema and Avro Protocol languages, allowing language injection</li>
			</ul>
			<p>Version 0.2.0:</p>
			<ul data-version="0.2.0">
			<li>Add support for imports</li>
			<li>Add file types and JSON schemas for <code>.avsc</code> &amp; <code>.avpr</code></li>
			</ul>
			<p>Version 0.1.1:</p>
			<ul data-version="0.1.1">
			<li>Extra build for IntelliJ 2020.3</li>
			</ul>
			<p>Version 0.1.0:</p>
	        <ul data-version="0.1.0">
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
		// IntelliJ Community (IC), IntelliJ Ultimate (IU), PyCharm Community (PCC) &, PyCharm professional (PY) editions
		// with versions ranging from Fall 2020 to the latest release
		val intellijVersions = listOf("2020.3.3", "2021.1", "2021.1.2", "2021.2.3", "2021.3").flatMap(forIdes("IC", "IU"))
		val pycharmVersions = listOf("2020.3.3", "2021.1", "2021.1.2", "2021.2.3", "2021.3").flatMap(forIdes("PCC", "PY"))
		ideVersions.set(intellijVersions + pycharmVersions)
		failureLevel.set(EnumSet.complementOf(EnumSet.of(FailureLevel.DEPRECATED_API_USAGES)))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")
