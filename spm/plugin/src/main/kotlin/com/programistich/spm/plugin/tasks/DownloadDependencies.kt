package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin
import com.programistich.spm.plugin.deps.ProcessDependencies
import com.programistich.spm.plugin.models.SwiftPackage
import org.gradle.api.Project
import java.io.File

fun Project.downloadDependencies(swiftPackage: SwiftPackage) {
    val dependenciesFolder = File(project.buildDir, KotlinMultiplatformSwiftPackagePlugin.DEPENDENCIES)
    if (!dependenciesFolder.exists()) {
        dependenciesFolder.mkdirs()
    }

    swiftPackage.dependencies.forEach {
        ProcessDependencies.getSourceFolder(it, dependenciesFolder)
    }
}