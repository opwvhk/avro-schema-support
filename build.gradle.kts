@file:Suppress("SpellCheckingInspection")

import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import java.util.*

plugins {
	id("org.jetbrains.intellij") version "0.6.5"
	java
}

group = "net.sf.opk"
version = "0.1.1"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation("junit", "junit", "4.12")
	testImplementation("org.assertj", "assertj-core", "3.18.1")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
	version = "2020.2"
	setPlugins("com.intellij.java", "PsiViewer:202-SNAPSHOT.3")
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
	version(project.version)
	sinceBuild("202")
	//untilBuild("203.*")
	changeNotes(
		"""
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
tasks.getByName<org.jetbrains.intellij.tasks.RunPluginVerifierTask>("runPluginVerifier") {
	ideVersions(listOf("IC-2019.3.5", "IU-2019.3.5", "IC-2020.2.4", "IU-2020.2.4"))
	//version = "2019.3"
	//localPaths(listOf("/Applications/IntelliJ IDEA.app"))
	failureLevel = EnumSet.of(
		//FailureLevel.COMPATIBILITY_WARNINGS,
		//FailureLevel.COMPATIBILITY_PROBLEMS,
		FailureLevel.DEPRECATED_API_USAGES,
		FailureLevel.EXPERIMENTAL_API_USAGES,
		FailureLevel.INTERNAL_API_USAGES,
		//FailureLevel.OVERRIDE_ONLY_API_USAGES,
		//FailureLevel.NON_EXTENDABLE_API_USAGES,
		//FailureLevel.PLUGIN_STRUCTURE_WARNINGS,
		FailureLevel.MISSING_DEPENDENCIES,
		FailureLevel.INVALID_PLUGIN,
		FailureLevel.NOT_DYNAMIC
	)
}

sourceSets["main"].java.srcDirs("src/main/gen")
