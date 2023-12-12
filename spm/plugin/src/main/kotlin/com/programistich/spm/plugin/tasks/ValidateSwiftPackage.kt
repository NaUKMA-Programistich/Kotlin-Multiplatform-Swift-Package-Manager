package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin
import com.programistich.spm.plugin.models.SwiftPackage
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.io.File

internal fun Project.validateSwiftPackage(swiftPackage: SwiftPackage) {
   exec {
        workingDir = File(project.buildDir, KotlinMultiplatformSwiftPackagePlugin.FOLDER)
        commandLine = listOf("swift", "package", "describe")
        standardOutput = ByteArrayOutputStream()
    }
}
