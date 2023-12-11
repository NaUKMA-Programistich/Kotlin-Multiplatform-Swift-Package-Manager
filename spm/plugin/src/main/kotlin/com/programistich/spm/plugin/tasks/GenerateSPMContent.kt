package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin
import com.programistich.spm.plugin.models.SwiftPackage
import org.gradle.api.Project
import java.io.File

internal fun Project.generateSPMBuildFolder(swiftPackage: SwiftPackage) {
    val spmBuildFolder = File(project.buildDir, KotlinMultiplatformSwiftPackagePlugin.FOLDER)
    if (!spmBuildFolder.exists()) {
        spmBuildFolder.mkdirs()
    }

    val spmBuildFile = File(spmBuildFolder, "Package.swift")
    if (!spmBuildFile.exists()) {
        spmBuildFile.createNewFile()
    }

    val sourceFile = File(spmBuildFolder, "Sources")
    if (!sourceFile.exists()) {
        sourceFile.mkdirs()
    }

    val libraryFile = File(sourceFile, swiftPackage.packageName)
    if (!libraryFile.exists()) {
        libraryFile.mkdirs()
    }

    val sourceMainFile = File(libraryFile, "File.swift")
    if (!sourceMainFile.exists()) {
        sourceMainFile.createNewFile()
    }

    spmBuildFile.writeText(swiftPackage.generateContent())
}