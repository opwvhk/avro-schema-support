import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import java.util.*

plugins {
	`java-library`
	id("java")
	id("org.jetbrains.intellij") version "1.17.4"
}

val lastBuild = provider {
	file("jetbrains.lastBuild.txt").readLines()
		.asSequence()
		.map { it.trim() }
		.filterNot { it.isEmpty() || it.startsWith("#") }
		.map { Integer.valueOf(it) }
		.map { it + 1 }
		.map { "$it.*" }
		.first()
}

group = "net.sf.opk"
version = "223.3.1"

repositories {
	mavenCentral()
	mavenLocal()
}
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

dependencies {
	implementation("org.apache.avro:avro-idl:1.12.0") { exclude("org.slf4j") }
	implementation("org.apache.commons:commons-compress:1.26.2")
	implementation("org.apache.commons:commons-text:1.12.0")
	implementation("org.apache.commons:commons-lang3:3.15.0")
	implementation("org.json:json:20240303")
	implementation("org.kohsuke", "github-api", "1.317")
	implementation("io.jsonwebtoken", "jjwt-impl", "0.10.5")
	implementation("io.jsonwebtoken", "jjwt-jackson", "0.10.5")
	testImplementation("junit", "junit", "4.13.2")
	testImplementation("org.assertj", "assertj-core", "3.24.2")
}

idea {
	module {
		isDownloadSources = true
	}
}
// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	downloadSources.set(true)

	// JetBrains advises to support the current and at least 2 previous major releases. The last major release is used
	// by about half of the users, the last 3 major releases by approx. 80%.
	// In March 2024, this means versions >= 2022.3.
	// Source: https://plugins.jetbrains.com/docs/marketplace/product-versions-in-use-statistics.html

	// Set type (Intellij/PyCharm Community), version, base plugin and PSI Viewer plugin at once:
	// Last minor versions differ, and the PSIViewer versions are not regular
	// Also, tests require the base plugin (java/PythonCore; so don't remove it even if the plugin does not need it)

	type.set("IC")
	plugins.addAll("com.intellij.java")
	//version.set("2022.1.4")
	version.set("2022.3.3")
	//version.set("2023.1.6")
	// From here, refactor code for compatibility / deprecated code usage
	//version.set("2023.2.6")
	//version.set("2023.3.6")
	//version.set("2024.1.4")
	// EAP
	//version.set("2024.2")

	//type.set("PC")
	//plugins.addAll("PythonCore")
	//version.set("2022.3.3")
	//version.set("2023.1.5")
	// From here, refactor code for compatibility / deprecated code usage
	//version.set("2023.2.6")
	//version.set("2023.3.5")
	//version.set("2024.1.4")
	// EAP
	//version.set("2024.2")

	// Extra plugin(s); not needed, but maybe useful during development:
	plugins.add("markdown")
	@Suppress("UNUSED_VARIABLE") val lombokPluginName = "Lombook Plugin" // Yes, the typo is part of the official name
	@Suppress("UNUSED_VARIABLE") val editorConfigPluginName = "org.editorconfig.editorconfigjetbrains"
	/* Other (bundled) plugins: */
	/*
	plugins.add("org.intellij.intelliLang")
	plugins.add("Git4Idea")
	plugins.add("com.intellij.tasks")
	plugins.add(lombokPluginName)
	plugins.add("org.intellij.plugins.markdown")
	plugins.add("org.jetbrains.idea.maven")
	plugins.add("org.jetbrains.kotlin")
	plugins.add(editorConfigPluginName)
	plugins.add("org.jetbrains.plugins.github")
	plugins.add("org.jetbrains.idea.maven.model")
	plugins.add("com.intellij.copyright")
	*/
}

tasks {
	withType<JavaCompile> {
		options.compilerArgs.add("-Xlint:unchecked")
		options.isDeprecation = true
	}
	test {
		systemProperty("idea.force.use.core.classloader", "true")
	}

	runIde {
		jvmArgs("-XX:+UnlockDiagnosticVMOptions")
	}

	patchPluginXml {
		version.set(project.version.toString())
		// Notes on versions: usually the last 3 major releases represent about 80% of the users.
		// See https://plugins.jetbrains.com/docs/marketplace/product-versions-in-use-statistics.html for more information.
		//sinceBuild.set("221") // Version 2022.1
		sinceBuild.set("223") // Version 2022.3
		// Find last EAP version (the build version until the first dot):
		// curl 'https://data.services.jetbrains.com/products/releases?code=IIU&code=IIC&code=PCP&code=PCC&latest=true&type=eap' 2>/dev/null|jq -r '.[][0].build'|cut -d . -f 1|sort -r|head -n 1
		untilBuild.set(lastBuild)
		//language=HTML
		var changeLog = """
			<p>Version 223.3.1:</p><ul>
				<li>Added IDL syntax for the schema syntax (new in Avro 1.12.0)</li>
				<li>Added inspection suggesting the schema syntax where appropriate</li>
				<li>Make update notifications more resilient</li>
				<li>Fix exception on intention preview</li>
			</ul>
			<p>Version 223.3.0:</p><ul>
				<li>Using IntelliJ version 2022.3.3 to test</li>
				<li>Refactor code for new Java & SDK versions</li>
				<li>Fix cosmetic bug in error report</li>
			</ul>
		"""
		changeLog += """
			<p>Version 221.4.3:</p><ul>
				<li>Fix regression in update notifications</li>
			</ul>
			<p>Version 221.4.2:</p><ul>
				<li>Improved the JSON-Schemata for <code>.avsc</code> and <code>.avpr</code> files</li>
				<li>Redesigned update notice to match new UI style</li>
				<li>Report errors using a GitHub app</li>
			</ul>
			<p>Version 221.4.1:</p><ul>
				<li>Added new plugin logo and updated icons</li>
			</ul>
			<p>Version 221.4.0:</p><ul>
				<li>Using IntelliJ version 2021.4 to test</li>
			</ul>
		"""
		//changeLog += """
		//	<p>Version 213.5.3:</p><ul>
		//		<li>Improved grammar (string literal syntax)</li>
		//		<li>Implemented better symbols (adds symbol search)</li>
		//		<li>Fix #106: converting to IDL now handles optional collections correctly
		//	</ul>
		//	<p>Version 213.5.2:</p><ul>
		//		<li>Improved grammar (fixes #82, #83)</li>
		//		<li>Added duplicate name detection (fixes #84; does not evaluate aliases)</li>
		//		<li>Fixed #91: improper range in JSON string literal</li>
		//		<li>Added reference detection to JSON strings other than in import statements (#92)</li>
		//	</ul>
		//	<p>Version 213.5.1:</p><ul>
		//		<li>Upgraded dependencies</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.5.0:</p><ul>
		//		<li>
		//			Renamed internal language ID (issue #78).<br/>
		//			<b>This invalidates previously generated <code>.editorconfig</code> settings!</b><br/>
		//			To fix, replace "ij_avro idl_" with "ij_avroidl_" in your <code>.editorconfig</code> files.
		//		</li>
		//		<li>Added disposed check in menu actions (issue #73)</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.4.3:</p><ul>
		//		<li>Fixed issue #77: editor support for angle brackets was broken</li>
		//	</ul>
		//	<p>Version 213.4.2:</p><ul>
		//		<li>Updated API token for crash reporting</li>
		//	</ul>
		//	<p>Version 213.4.1:</p><ul>
		//		<li>Fix #64 (bug in formatting preferences)</li>
		//		<li>Layout change for code style tab "Other"</li>
		//		<li>Reverted code to help diagnose crashes</li>
		//	</ul>
		//	<p>Version 213.4.0:</p><ul>
		//		<li>Added code to help diagnose issues #39, #43 & #44 (NoClassDefFoundError for existing class)</li>
		//		<li>Implement #42: Renaming a schema or field now adds an alias for the old name</li>
		//		<li>Add new settings for #42, making the behaviour configurable (by default, only fields receive an alias)</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.3.1:</p><ul>
		//		<li>Fix #38 (Incorrect error message in JSON files with multiple invalid names)</li>
		//		<li>Fix #40 (cannot set caret in inspection actions when used for preview)</li>
		//	</ul>
		//	<p>Version 213.3.0:</p><ul>
		//		<li>Fix #36 (incorrectly recognizing nullable primitive types)</li>
		//		<li>Fix #37 (generating IDL can yield invalid names with Avro <= 1.11.0)</li>
		//		<li>Added error report submitter (submit crash reports directly to GitHub)</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.2.1:</p><ul>
		//		<li>Fixed bug #27 (a ClassCastException)</li>
		//		<li>Cleaned up the code (to prevent more bugs like #27)</li>
		//	</ul>
		//	<p>Version 213.2.0:</p><ul>
		//		<li>Using Avro 1.11.1 for conversions</li>
		//		<li>Added support for Kotlin style nullable types (new in Avro 1.11.1)</li>
		//		<li>Added inspection for documentation comments to detect and apply fixes for improvements since Avro 1.11.1</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.1.0:</p><ul>
		//		<li>Added quick fixes for missing schema / enum symbol references</li>
		//		<li>Added declaration documentation for "quick info" popups</li>
		//		<li>Added move left/right handing for list-like syntax</li>
		//		<li>Improved brace handling</li>
		//		<li>Improved references to schemata in JSON (<code>.avsc</code>/<code>.avpr</code>)</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 213.0.1:</p><ul>
		//		<li>Fixed issue #22</li>
		//	</ul>
		//	<p>Version 213.0.0:</p><ul>
		//		<li>New File dialog for IDL files</li>
		//		<li>New: inspections for naming conventions and warnings</li>
		//		<li>Improved refactoring actions (converting IDL from/to schema/protocol files)</li>
		//		<li>Extended formatting & syntax highlighting options</li>
		//		<li>Improved quote & brace handling: now supports all "", {}, [] and <> pairs</li>
		//		<li>Using IntelliJ version 2021.3 to test</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 203.1.2:</p><ul>
		//		<li>Fixed exception when writing a schema as IDL (issue #16)</li>
		//		<li>Improved formatting of message properties</li>
		//		<li>Improved file import references</li>
		//		<li>Fixed range exception in references</li>
		//		<li>Added error for annotations on references (triggers a bug in Avro &lt; 1.11.1, breaks in later Avro versions)</li>
		//		<li>Improve identifier parsing to match the Avro IDL grammar</li>
		//	</ul>
		//	<p>Version 203.1.1:</p><ul>
		//		<li>Fixed NPE in import resolution (issue #14)</li>
		//		<li>Adjusted IDL parsing: improved detection of annotations for messages</li>
		//		<li>Adjusted IDL parsing: allows for more dangling doc comments as allowed by the official parser</li>
		//	</ul>
		//	<p>Version 203.1.0:</p><ul>
		//		<li>Adjusted IDL parsing to allow dangling doc comments</li>
		//		<li>Added warnings for dangling doc comments (the Avro IDL compiler ignores these)</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 203.0.3:</p><ul>
		//		<li>Added startup check for incompatibilities (i.e., is the other Avro plugin active?)</li>
		//	</ul>
		//	<p>Version 203.0.2:</p><ul>
		//		<li>IDL parser now also allows annotations before doc comments (issue #12).</li>
		//	</ul>
		//	<p>Version 203.0.1:</p><ul>
		//		<li>Fixed NPEs upon file traversal for plugin actions (issue #9, #11).</li>
		//		<li>Renamed defined languages to prevent name clashes.</li>
		//	</ul>
		//	<p>Version 203.0.0:</p><ul>
		//		<li>Changed version number to match IntelliJ builds</li>
		//		<li>Migrated away from deprecated API: minimum supported version is now 2020.3</li>
		//		<li>Added refactoring actions to convert Avro IDL to and from Avro schemas and Avro protocols.</li>
		//		<li>Added file icon variant for dark mode</li>
		//	</ul>
		//	"""
		//changeLog += """
		//	<p>Version 0.2.1:</p><ul>
		//		<li>Add Avro Schema and Avro Protocol languages, allowing language injection</li>
		//	</ul>
		//	<p>Version 0.2.0:</p><ul>
		//		<li>Add support for imports</li>
		//		<li>Add file types and JSON schemas for <code>.avsc</code> &amp; <code>.avpr</code></li>
		//	</ul>
		//	<p>Version 0.1.1:</p><ul>
		//		<li>Extra build for IntelliJ 2020.3</li>
		//	</ul>
		//	<p>Version 0.1.0:</p><ul>
		//		<li>Initial release</li>
		//		<li>Full parsing of Avro .avdl files, based on Avro 1.10 syntax</li>
		//		<li>Syntax highlighting & formatting</li>
		//		<li>Code completion based on syntax and supported references</li>
		//		<li>Some semantic checks</li>
		//		<li>Some refactoring support (renaming & deleting named types)</li>
		//    </ul>
		//	"""
		changeNotes.set(changeLog)
	}

	runPluginVerifier {
		// Do not set ideVersions: the default is to use the result of the listProductsReleases task
		failureLevel.set(
			EnumSet.complementOf(
				EnumSet.of(
					FailureLevel.SCHEDULED_FOR_REMOVAL_API_USAGES,
					FailureLevel.DEPRECATED_API_USAGES,
					FailureLevel.EXPERIMENTAL_API_USAGES
				)
			)
		)
	}

	listProductsReleases {
		sinceBuild.set(patchPluginXml.get().sinceBuild.get()) // Oldest supported version
		untilVersion.set("*") // No upper bound
		// Do not set releaseChannels: the default is all
		types.set(listOf("IC", "IU", "PC", "PY")) // Default: only IC

		// To save time (but a less complete check) is to keep only the last patch release of each minor release:
		// curl 'https://data.services.jetbrains.com/products/releases?code=IIU&code=IIC&code=PCP&code=PCC&type=eap&type=release' 2>/dev/null |
		//   jq -r 'to_entries|map({key,"value":.value|map({majorVersion,version}|select(.majorVersion|test("202.\\..")))|unique|group_by(.majorVersion)|map(last(.[]))|map(.version)}|{key,"version":.value[]}|(.key+"-"+.version))|.[]'
	}

	/** Fail the build if it has a SNAPSHOT version */
	register<DefaultTask>("requireNonSnapshotBuild") {
		doFirst {
			val version = project.version.toString()
			if (version == "unspecified") {
				throw GradleException("No version set")
			}
			if (version.endsWith("-SNAPSHOT")) {
				throw GradleException("Cannot publish snapshot builds")
			}
		}
	}

	/** Copies files from "build/distributions" to "demo" directory */
	register<Copy>("archiveBuildArtifact") {
		println("Archiving Build Artifacts")
		from(layout.buildDirectory.dir("distributions"))
		include("**/*.*")
		into(layout.projectDirectory.dir("../archive-avro-schema-support"))
	}

	publishPlugin {
		dependsOn("requireNonSnapshotBuild")
		finalizedBy("archiveBuildArtifact")

		// jetbrainsToken is set in ~/.gradle/gradle.properties, so the secret is not stored in git
		token.set(providers.gradleProperty("jetbrainsToken"))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")
