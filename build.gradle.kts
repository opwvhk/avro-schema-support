import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.extensions.IntelliJPlatformDependenciesExtension
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel.*
import org.jetbrains.intellij.pluginRepository.PluginRepository
import org.jetbrains.intellij.pluginRepository.PluginRepositoryFactory
import java.util.*

val IntelliJPlatformDependenciesExtension.pluginRepository: PluginRepository by lazy {
	PluginRepositoryFactory.create("https://plugins.jetbrains.com")
}

fun IntelliJPlatformDependenciesExtension.pluginsInLatestCompatibleVersion(vararg pluginIds: String): Unit =
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
	id("org.jetbrains.intellij.platform") version "2.20.0"
	id("java")
	`java-library`
}

// The first and last supported builds
// The first build MUST be compatible with the platform & version in dependencies/intellijPlatform !
// The last build is one more than the last release (to accommodate the EAP before it is released)
val firstBuild = "253"
val lastBuild = provider {
	file("jetbrains.lastBuild.txt").readLines()
		.asSequence()
		.map { it.trim() }
		.filterNot { it.isEmpty() || it.startsWith("#") }
		.map { Integer.valueOf(it) }
		.map { "$it.*" }
		.first()
}

group = "net.sf.opk"
version = "253.0.1-SNAPSHOT"

repositories {
	mavenLocal()
	mavenCentral()
	intellijPlatform {
		defaultRepositories()
	}
}
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
		//languageVersion.set(JavaLanguageVersion.of(25))
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")

dependencies {
	intellijPlatform {
		// JetBrains advises to support the current and at least 2 previous major releases. The last major release is used
		// by about half of the users, the last 3 major releases by approx. 80%.
		// In March 2024, this means versions >= 2022.3.
		// Source: https://plugins.jetbrains.com/docs/marketplace/product-versions-in-use-statistics.html

		// Set type (Intellij/PyCharm), version, base plugin and PSI Viewer plugin at once:
		// Last minor versions differ, and the PSIViewer versions are not regular
		// Also, tests require the base plugin (java/PythonCore; so don't remove it even if the plugin does not need it)

		intellijIdea("2025.3.6.1")
		//intellijIdea("2026.1.5")
		//intellijIdea("2026.2.3")
		bundledPlugin("com.intellij.java")

		//pycharm("2025.3.6.1")
		//pycharm("2026.1.5")
		//pycharm("2026.2.3")
		//bundledPlugin("PythonCore")

		// Plugin dependencies (for optional dependencies in the plugin.xml):
		bundledPlugin("com.intellij.modules.json")
		bundledModule("org.intellij.intelliLang")
		bundledModule("intellij.platform.langInjection")
		// For 2026.2 or later, the structure view has become a separate module
		//bundledModule("intellij.platform.structureView")

		// Extra plugin(s); not needed for the plugin, but maybe useful during development:
		pluginsInLatestCompatibleVersion("PsiViewer")
		bundledPlugin("org.intellij.plugins.markdown")
		/* Other (bundled) plugins: */
		// Define this variable to prevent spell checking errors in the comment below
		@Suppress("SpellCheckingInspection", "unused", "UnusedVariable")
		val lombokPluginName = "Lombook Plugin" // Yes, the typo is part of the official name

		pluginVerifier()
		zipSigner()

		testFramework(TestFrameworkType.Platform)
	}

	implementation("org.jetbrains:annotations:26.1.0")
	implementation("com.fasterxml.jackson.core:jackson-core:2.22.4")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.22.4")
	implementation("org.apache.avro:avro-idl:1.12.2") { exclude("org.slf4j") }
	implementation("org.apache.commons:commons-compress:1.28.0")
	implementation("org.apache.commons:commons-text:1.15.0")
	implementation("org.apache.commons:commons-lang3:3.21.0")
	implementation("org.json:json:20260814")
	implementation("org.kohsuke:github-api:1.330")
	implementation("io.jsonwebtoken:jjwt-impl:0.13.0")
	implementation("io.jsonwebtoken:jjwt-jackson:0.13.0")
	testImplementation("junit:junit:4.13.2")
	testImplementation("org.assertj:assertj-core:3.27.7")
}

intellijPlatform {
	splitMode.set(false)
	pluginConfiguration {
		ideaVersion {
			sinceBuild.set(firstBuild)
			untilBuild.set(lastBuild)
		}
		changeNotes.set(
			file("CHANGELOG.xml").readLines().joinToString("\n")
				.replace(Regex("<!--(?!-->).*-->"), "") // Remove comments
				.replaceFirst(Regex("^<[^>][^>]*>"), "") // Remove wrapping tag
				.replaceFirst(Regex("</[^>][^>]*>$"), "")
		)
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
	@Suppress("DEPRECATION") // False positive
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
		description = """
			Fails the build if the version is a snapshot version (i.e., ends with -SNAPSHOT) or is not set.
			This is required by JetBrains Marketplace, and prevents accidentally publishing a broken plugin.
			""".trimIndent()
		finalizedBy("compileJava")
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
		description = "Copies the build artifact to a sibling folder."
		dependsOn("buildPlugin")
		doFirst {
			project.logger.lifecycle("Archiving Build Artifacts")
		}
		from(layout.buildDirectory.dir("distributions"))
		include("**/*.*")
		into(layout.projectDirectory.dir("../archive-avro-schema-support"))
	}

	publishPlugin {
		dependsOn("requireNonSnapshotBuild")
		finalizedBy("archiveBuildArtifact")
	}
}
