import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel.*
import org.jetbrains.intellij.pluginRepository.PluginRepositoryFactory
import java.util.*

val IntelliJPlatformDependenciesExtension.pluginRepository by lazy {
	PluginRepositoryFactory.create("https://plugins.jetbrains.com")
}

fun IntelliJPlatformDependenciesExtension.pluginsInLatestCompatibleVersion(vararg pluginIds: String) =
	plugins(provider {
		pluginIds.map { pluginId ->
			val platformType = intellijPlatform.productInfo.productCode
			val platformVersion = intellijPlatform.productInfo.buildNumber

			val plugin = pluginRepository.pluginManager.searchCompatibleUpdates(
				build = "$platformType-$platformVersion",
				xmlIds = listOf(pluginId),
			).firstOrNull()
				?: throw GradleException("No plugin update with id='$pluginId' compatible with '$platformType-$platformVersion' found in JetBrains Marketplace")

			"${plugin.pluginXmlId}:${plugin.version}"
		}
	})

plugins {
	`java-library`
	id("java")
	id("org.jetbrains.intellij.platform") version "2.10.5"
}

// The first and last supported builds
// The first build MUST be compatible with the platform & version in dependencies/intellijPlatform !
// The last build is one more than the last release (to accommodate the EAP before it is released)
val firstBuild = "243"
val lastBuild = provider {
	file("jetbrains.lastBuild.txt").readLines()
		.asSequence()
		.map { it.trim() }
		.filterNot { it.isEmpty() || it.startsWith("#") }
		.map { Integer.valueOf(it) }
		.map { "$it.*" }
		.first()
}!!

group = "net.sf.opk"
version = "243.0.3-SNAPSHOT"

repositories {
	mavenLocal()
	mavenCentral()
	intellijPlatform {
		defaultRepositories()
	}
}
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {
	intellijPlatform {
		// JetBrains advises to support the current and at least 2 previous major releases. The last major release is used
		// by about half of the users, the last 3 major releases by approx. 80%.
		// In March 2024, this means versions >= 2022.3.
		// Source: https://plugins.jetbrains.com/docs/marketplace/product-versions-in-use-statistics.html

		// Set type (Intellij/PyCharm Community), version, base plugin and PSI Viewer plugin at once:
		// Last minor versions differ, and the PSIViewer versions are not regular
		// Also, tests require the base plugin (java/PythonCore; so don't remove it even if the plugin does not need it)

		intellijIdeaCommunity("2024.3.6")
		//intellijIdeaCommunity("2025.1.5.1")
		//intellijIdeaCommunity("2025.2.4")
		//intellijIdea("2025.3")
		// EAP
		bundledPlugin("com.intellij.java")

		//pycharmCommunity("2024.3.6")
		//pycharm("2025.1.3.1")
		//pycharm("2025.2.1.1")
		// EAP
		//bundledPlugin("PythonCore")

		// Plugin dependencies (for optional dependencies in the plugin.xml):
		bundledPlugin("com.intellij.modules.json")
		bundledPlugin("org.intellij.intelliLang")
		// When targeting 2025.3 or later, language injection becomes a (bundled) module:
		//bundledModule("intellij.platform.langInjection")

		// Extra plugin(s); not needed for the plugin, but maybe useful during development:
		pluginsInLatestCompatibleVersion("PsiViewer")
		bundledPlugin("org.intellij.plugins.markdown")
		/* Other (bundled) plugins: */
		// Define these variables to prevent spell checking errors in the comment below
		@Suppress("UNUSED_VARIABLE", "unused")
		val lombokPluginName = "Lombook Plugin" // Yes, the typo is part of the official name
		@Suppress("UNUSED_VARIABLE", "unused")
		val editorConfigPluginName = "org.editorconfig.editorconfigjetbrains"
		/*
		bundledPlugin("Git4Idea")
		bundledPlugin("com.intellij.tasks")
		bundledPlugin(lombokPluginName)
		bundledPlugin("org.jetbrains.idea.maven")
		bundledPlugin("org.jetbrains.kotlin")
		bundledPlugin(editorConfigPluginName)
		bundledPlugin("org.jetbrains.plugins.github")
		bundledPlugin("org.jetbrains.idea.maven.model")
		bundledPlugin("com.intellij.copyright")
		*/

		pluginVerifier()
		zipSigner()

		testFramework(TestFrameworkType.Platform)
	}

	implementation("org.apache.avro:avro-idl:1.12.1") { exclude("org.slf4j") }
	implementation("org.apache.commons:commons-compress:1.28.0")
	implementation("org.apache.commons:commons-text:1.14.0")
	implementation("org.apache.commons:commons-lang3:3.20.0")
	implementation("org.json:json:20250517")
	implementation("org.kohsuke:github-api:1.327")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
	testImplementation("junit:junit:4.13.2")
	testImplementation("org.assertj:assertj-core:3.27.6")
}

intellijPlatform {
	pluginConfiguration {
		ideaVersion {
			sinceBuild.set(firstBuild)
			untilBuild.set(lastBuild)
		}
		changeNotes.set("""
			<p>Version 243.0.3:</p><ul>
				<li>...</li>
			</ul>
			<p>Version 243.0.2:</p><ul>
				<li>Fix crash/issue #267</li>
			</ul>
			<p>Version 243.0.1:</p><ul>
				<li>Add support for JetBrains version 2025.3</li>
			</ul>
			<p>Version 243.0.0:</p><ul>
				<li>Upgraded minimum supported JetBrains version to 2024.3</li>
				<li>Disable upgrade notification links (avoids #230)</li>
				<li>Fix bug #249 (a threading issue)</li>
			</ul>
			<p>Version 241.0.2:</p><ul>
				<li>Add support for JetBrains version 2025.2</li>
				<li>Fix bugs #222 and #223 (IDL syntax handling)</li>
			</ul>
			<p>Version 241.0.1:</p><ul>
				<li>
					Made grammar more lenient: any Unicode/Java identifier is valid as-is, and any string when quoted by
					backticks (backticks are quoted by doubling them). This causes less parse errors, and makes future
					error messages more flexible. As the Avro language is much more strict, there is no effective
					difference.
				</li>
			</ul>
			<p>Version 241.0.0:</p><ul>
				<li>Upgraded minimum supported JetBrains version to 2024.1</li>
				<li>Remove FileType instance references form JSON schema code</li>
				<li>Reenable JSON compliance checks (they were rewritten as inspections). Fixes #213.</li>
			</ul>
		""".trimIndent())
		/* Older changelog entries:
			<p>Version 232.0.2:</p><ul>
				<li>Fix filetype names</li>
				<li>Improve text bundles for 2025.1 requirements</li>
			</ul>
			<p>Version 232.0.1:</p><ul>
				<li>Improve threading for previewing quick fixes</li>
				<li>Improve/refactor action tests</li>
			</ul>
			<p>Version 232.0.0:</p><ul>
				<li>Upgraded minimum supported JetBrains version to 2023.2</li>
				<li>Add explicit dependency to JSON module</li>
				<li>Add Language injections for Avro</li>
			</ul>
			<p>Version 223.3.3:</p><ul>
				<li>Use EDT when previewing quick fixes (fixes #177)</li>
			</ul>
			<p>Version 223.3.2:</p><ul>
				<li>Debug refactoring actions (fixes #168, #169)</li>
				<li>Add workaround for JetBrains SDK bug (hopefully mitigates #127)</li>
				<li>Use newline instead of platform linefeed when generating content (fixes #163)</li>
				<li>Add better error message to IDL conversion (issue #167)</li>
				<li>Wrap actions in 'commands' for undo (fixes #160, #161)</li>
				<li>Refactored hardcoded texts into a resource bundle</li>
			</ul>
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
			<p>Version 213.5.3:</p><ul>
				<li>Improved grammar (string literal syntax)</li>
				<li>Implemented better symbols (adds symbol search)</li>
				<li>Fix #106: converting to IDL now handles optional collections correctly
			</ul>
			<p>Version 213.5.2:</p><ul>
				<li>Improved grammar (fixes #82, #83)</li>
				<li>Added duplicate name detection (fixes #84; does not evaluate aliases)</li>
				<li>Fixed #91: improper range in JSON string literal</li>
				<li>Added reference detection to JSON strings other than in import statements (#92)</li>
			</ul>
			<p>Version 213.5.1:</p><ul>
				<li>Upgraded dependencies</li>
			</ul>
			<p>Version 213.5.0:</p><ul>
				<li>
					Renamed internal language ID (issue #78).<br/>
					<b>This invalidates previously generated <code>.editorconfig</code> settings!</b><br/>
					To fix, replace "ij_avro idl_" with "ij_avroidl_" in your <code>.editorconfig</code> files.
				</li>
				<li>Added disposed check in menu actions (issue #73)</li>
			</ul>
			<p>Version 213.4.3:</p><ul>
				<li>Fixed issue #77: editor support for angle brackets was broken</li>
			</ul>
			<p>Version 213.4.2:</p><ul>
				<li>Updated API token for crash reporting</li>
			</ul>
			<p>Version 213.4.1:</p><ul>
				<li>Fix #64 (bug in formatting preferences)</li>
				<li>Layout change for code style tab "Other"</li>
				<li>Reverted code to help diagnose crashes</li>
			</ul>
			<p>Version 213.4.0:</p><ul>
				<li>Added code to help diagnose issues #39, #43 & #44 (NoClassDefFoundError for existing class)</li>
				<li>Implement #42: Renaming a schema or field now adds an alias for the old name</li>
				<li>Add new settings for #42, making the behaviour configurable (by default, only fields receive an alias)</li>
			</ul>
			<p>Version 213.3.1:</p><ul>
				<li>Fix #38 (Incorrect error message in JSON files with multiple invalid names)</li>
				<li>Fix #40 (cannot set caret in inspection actions when used for preview)</li>
			</ul>
			<p>Version 213.3.0:</p><ul>
				<li>Fix #36 (incorrectly recognizing nullable primitive types)</li>
				<li>Fix #37 (generating IDL can yield invalid names with Avro <= 1.11.0)</li>
				<li>Added error report submitter (submit crash reports directly to GitHub)</li>
			</ul>
			<p>Version 213.2.1:</p><ul>
				<li>Fixed bug #27 (a ClassCastException)</li>
				<li>Cleaned up the code (to prevent more bugs like #27)</li>
			</ul>
			<p>Version 213.2.0:</p><ul>
				<li>Using Avro 1.11.1 for conversions</li>
				<li>Added support for Kotlin style nullable types (new in Avro 1.11.1)</li>
				<li>Added inspection for documentation comments to detect and apply fixes for improvements since Avro 1.11.1</li>
			</ul>
			<p>Version 213.1.0:</p><ul>
				<li>Added quick fixes for missing schema / enum symbol references</li>
				<li>Added declaration documentation for "quick info" popups</li>
				<li>Added move left/right handing for list-like syntax</li>
				<li>Improved brace handling</li>
				<li>Improved references to schemata in JSON (<code>.avsc</code>/<code>.avpr</code>)</li>
			</ul>
			<p>Version 213.0.1:</p><ul>
				<li>Fixed issue #22</li>
			</ul>
			<p>Version 213.0.0:</p><ul>
				<li>New File dialog for IDL files</li>
				<li>New: inspections for naming conventions and warnings</li>
				<li>Improved refactoring actions (converting IDL from/to schema/protocol files)</li>
				<li>Extended formatting & syntax highlighting options</li>
				<li>Improved quote & brace handling: now supports all "", {}, [] and <> pairs</li>
				<li>Using IntelliJ version 2021.3 to test</li>
			</ul>
			<p>Version 203.1.2:</p><ul>
				<li>Fixed exception when writing a schema as IDL (issue #16)</li>
				<li>Improved formatting of message properties</li>
				<li>Improved file import references</li>
				<li>Fixed range exception in references</li>
				<li>Added error for annotations on references (triggers a bug in Avro &lt; 1.11.1, breaks in later Avro versions)</li>
				<li>Improve identifier parsing to match the Avro IDL grammar</li>
			</ul>
			<p>Version 203.1.1:</p><ul>
				<li>Fixed NPE in import resolution (issue #14)</li>
				<li>Adjusted IDL parsing: improved detection of annotations for messages</li>
				<li>Adjusted IDL parsing: allows for more dangling doc comments as allowed by the official parser</li>
			</ul>
			<p>Version 203.1.0:</p><ul>
				<li>Adjusted IDL parsing to allow dangling doc comments</li>
				<li>Added warnings for dangling doc comments (the Avro IDL compiler ignores these)</li>
			</ul>
			<p>Version 203.0.3:</p><ul>
				<li>Added startup check for incompatibilities (i.e., is the other Avro plugin active?)</li>
			</ul>
			<p>Version 203.0.2:</p><ul>
				<li>IDL parser now also allows annotations before doc comments (issue #12).</li>
			</ul>
			<p>Version 203.0.1:</p><ul>
				<li>Fixed NPEs upon file traversal for plugin actions (issue #9, #11).</li>
				<li>Renamed defined languages to prevent name clashes.</li>
			</ul>
			<p>Version 203.0.0:</p><ul>
				<li>Changed version number to match IntelliJ builds</li>
				<li>Migrated away from deprecated API: minimum supported version is now 2020.3</li>
				<li>Added refactoring actions to convert Avro IDL to and from Avro schemas and Avro protocols.</li>
				<li>Added file icon variant for dark mode</li>
			</ul>
			<p>Version 0.2.1:</p><ul>
				<li>Add Avro Schema and Avro Protocol languages, allowing language injection</li>
			</ul>
			<p>Version 0.2.0:</p><ul>
				<li>Add support for imports</li>
				<li>Add file types and JSON schemas for <code>.avsc</code> &amp; <code>.avpr</code></li>
			</ul>
			<p>Version 0.1.1:</p><ul>
				<li>Extra build for IntelliJ 2020.3</li>
			</ul>
			<p>Version 0.1.0:</p><ul>
				<li>Initial release</li>
				<li>Full parsing of Avro .avdl files, based on Avro 1.10 syntax</li>
				<li>Syntax highlighting & formatting</li>
				<li>Code completion based on syntax and supported references</li>
				<li>Some semantic checks</li>
				<li>Some refactoring support (renaming & deleting named types)</li>
		    </ul>
		*/
	}
	pluginVerification {
		failureLevel.set(EnumSet.complementOf(EnumSet.of(
			SCHEDULED_FOR_REMOVAL_API_USAGES, DEPRECATED_API_USAGES
		)))
		ides {
			recommended()
		}
	}
	publishing {
		// jetbrainsToken is set in ~/.gradle/gradle.properties, so the secret is not stored in git
		token.set(providers.gradleProperty("jetbrainsToken"))
	}
}

idea {
	module {
		isDownloadSources = true
	}
}

tasks {
	compileJava {
		options.compilerArgs.add("-Xlint:unchecked")
		options.isDeprecation = true
	}
	runIde {
		jvmArgs("-XX:+UnlockDiagnosticVMOptions")
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
		dependsOn("buildPlugin")
		println("Archiving Build Artifacts")
		from(layout.buildDirectory.dir("distributions"))
		include("**/*.*")
		into(layout.projectDirectory.dir("../archive-avro-schema-support"))
	}

	publishPlugin {
		dependsOn("requireNonSnapshotBuild")
		finalizedBy("archiveBuildArtifact")
	}
}
