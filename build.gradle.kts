import org.jetbrains.intellij.tasks.ListProductsReleasesTask.Channel.*
import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import java.util.*

plugins {
	`java-library`
	id("java")
	id("org.jetbrains.intellij") version "1.5.3"
}

val lastBuild = provider {
	file("jetbrains.lastBuild.txt").readLines()
		.map { it.trim() }
		.filterNot { it.isEmpty() || it.startsWith("#") }
		.first() + ".*"
}

group = "net.sf.opk"
version = "213.1.0-SNAPSHOT"

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

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	//version.set("2021.2.4")
	//plugins.set(listOf("com.intellij.java", "PsiViewer:212-SNAPSHOT")) // Add the java plugin here to satisfy test dependencies.
	version.set("2021.3.3")
	plugins.set(listOf("com.intellij.java", "PsiViewer:213-SNAPSHOT")) // Add the java plugin here to satisfy test dependencies.
	//version.set("2022.1")
	//plugins.set(listOf("com.intellij.java", "PsiViewer:221-SNAPSHOT")) // Add the java plugin here to satisfy test dependencies.
}

tasks {
	withType<JavaCompile> {
		options.compilerArgs.add("-Xlint:unchecked")
		options.isDeprecation = true
	}
	test {
		systemProperty("idea.force.use.core.classloader", "true")
	}

	patchPluginXml {
		version.set(project.version.toString())
		sinceBuild.set("203")
		// Find last EAP version (the build version until the first dot):
		// curl 'https://data.services.jetbrains.com/products/releases?code=IIU&code=IIC&code=PCP&code=PCC&latest=true&type=eap' 2>/dev/null|jq -r '.[][0].build'|cut -d . -f 1|sort -r|head -n 1
		untilBuild.set(lastBuild)
		//untilBuild.set("221.*")
		changeNotes.set(
			/*
			<p>Version 213.2.0:</p>
			<ul data-version="213.1.0">
			<li>Added IDL syntax for the schema syntax (new in Avro 1.12.0)</li>
			<li>Added inspection suggesting the schema syntax where appropriate</li>
			</ul>
			<p>Version 213.1.0:</p>
			<ul data-version="213.1.0">
			<li>Added support for Kotlin style nullable types (new in Avro 1.11.1)</li>
			<li>Added inspection for documentation comments to detect and apply fixes for improvements since Avro 1.11.1</li>
			</ul>
			*/
			"""
			<p>Version 213.1.0:</p>
			<ul data-version="213.1.0">
			<li>Added quick fixes for missing schema / enum symbol references</li>
			</ul>
			<p>Version 213.0.1:</p>
			<ul data-version="213.0.1">
			<li>Fixed issue #22</li>
			</ul>
			<p>Version 213.0.0:</p>
			<ul data-version="213.0.0">
			<li>New File dialog for IDL files</li>
			<li>New: inspections for naming conventions and warnings</li>
			<li>Improved refactoring actions (converting IDL from/to schema/protocol files)</li>
			<li>Extended formatting & syntax highlighting options</li>
			<li>Improved quote & brace handling: now supports all "", {}, [] and <> pairs</li>
			</ul>
			<p>Version 203.1.2:</p>
			<ul data-version="203.1.2">
			<li>Fixed exception when writing a schema as IDL (issue #16)</li>
			<li>Improved formatting of message properties</li>
			<li>Improved file import references</li>
			<li>Fixed range exception in references</li>
			<li>Added error for annotations on references (triggers a bug in Avro &lt; 1.11.1, breaks in later Avro versions)</li>
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
			<li>Added startup check for incompatibilities (i.e., is the other Avro plugin active?)</li>
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
		// IntelliJ Community (IC), IntelliJ Ultimate (IU), PyCharm Community (PCC) &, PyCharm professional (PY) editions for the patch
		// releases for all minor releases ranging from sinceBuild to untilBuild (see the patchPluginXml task)
		val intellijVersions = listOf("2020.3.4", "2021.1.3", "2021.2.4", "2021.3.3", "2022.1").flatMap(forIdes("IC", "IU"))
		val pycharmVersions = listOf("2020.3.5", "2021.1.3", "2021.2.4", "2021.3.3", "2022.1").flatMap(forIdes("PCC", "PY"))
		ideVersions.set(intellijVersions + pycharmVersions)
		failureLevel.set(EnumSet.complementOf(EnumSet.of(FailureLevel.DEPRECATED_API_USAGES)))
	}

	listProductsReleases {
		sinceBuild.set("203")
		releaseChannels.set(listOf(RELEASE, EAP, PATCH, CANARY, RC))
		types.set(listOf("IC", "IU", "PC", "PY"))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")
