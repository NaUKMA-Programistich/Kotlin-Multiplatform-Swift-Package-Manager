package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.models.SwiftPackage
import com.programistich.spm.plugin.utils.SwiftPackageConstants
import com.programistich.spm.plugin.utils.createFileIfNotExist
import com.programistich.spm.plugin.utils.createFolderIfNotExist
import org.gradle.api.Project
import java.io.File

internal fun Project.generateSPMBuildFolder(swiftPackage: SwiftPackage) {
    val spmBuildFolder = File(project.buildDir, SwiftPackageConstants.FOLDER)
    spmBuildFolder.createFolderIfNotExist()

    val spmBuildFile = File(spmBuildFolder, "Package.swift")
    spmBuildFile.createFileIfNotExist()

    val sourceFile = File(spmBuildFolder, "Sources")
    sourceFile.createFolderIfNotExist()

    val libraryFile = File(sourceFile, checkNotNull(swiftPackage.packageName))
    libraryFile.createFolderIfNotExist()

    val sourceMainFile = File(libraryFile, "File.swift")
    sourceMainFile.createFileIfNotExist()

    spmBuildFile.writeText(swiftPackage.generateContent())
}
