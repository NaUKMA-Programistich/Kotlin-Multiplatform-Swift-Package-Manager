package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.deps.DependencyStorage
import com.programistich.spm.plugin.deps.Parser
import com.programistich.spm.plugin.models.Dependency
import com.programistich.spm.plugin.utils.SwiftPackageConstants
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

internal fun Project.parseDependencies() {
    DependencyStorage.getAll().forEach { (dependency, path) ->
        processParseDependency(path, dependency)
    }
}

private fun Project.processParseDependency(path: String, dependency: Dependency) {
    val commitName = path.substringAfterLast("/")
    val jsonDeps = File(path, "$commitName.json")

    val fromDeps = File(path).absolutePath.replace(" ", "\\ ")
    val toDeps = jsonDeps.absolutePath.replace(" ", "\\ ")

    val cli = listOf("bash", "-c", "swift run swift-package-tools --from=$fromDeps --to=$toDeps")
    val cliString = cli.joinToString(" ")

    val workDirectory = File(project.buildDir, SwiftPackageConstants.SPM_TOOLS_PATH)

    logger.warn("Processing dependencies for $dependency")
    logger.warn("Command: $cliString")
    logger.warn("Work directory: $workDirectory")

    val output = ByteArrayOutputStream()

    exec {
        workingDir = workDirectory
        commandLine = cli
        standardOutput = output
    }
    logger.warn("Output: $output")
    val parser = Json.decodeFromString<Parser>(jsonDeps.readText())
    DependencyStorage.setParser(dependency, parser)
}
