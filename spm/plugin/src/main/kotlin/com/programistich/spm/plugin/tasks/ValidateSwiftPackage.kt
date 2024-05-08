package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.utils.SwiftPackageConstants
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

internal fun Project.validateSwiftPackage() {
    val cli = listOf("bash", "-c", "swift package describe")

    val result = exec {
        workingDir = File(project.buildDir, SwiftPackageConstants.FOLDER)
        commandLine = cli
        standardOutput = ByteArrayOutputStream()
    }
    check(result.exitValue == 0) { "Swift package validation failed" }
}
