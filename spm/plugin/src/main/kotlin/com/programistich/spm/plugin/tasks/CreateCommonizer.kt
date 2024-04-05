package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.deps.DependencyStorage
import com.programistich.spm.plugin.findKotlinMultiplatformExtension
import com.programistich.spm.plugin.models.Dependency
import com.programistich.spm.plugin.utils.createFileIfNotExist
import com.programistich.spm.plugin.utils.createFolderIfNotExist
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.File

internal fun Project.createCommonizerTask() {
    val kmmExtension = this.findKotlinMultiplatformExtension() ?: return

    DependencyStorage.getAll().forEach { (dependency, path) ->
        val files = getAllFilesInFolderRecursive(path)

        kmmExtension.targets.withType<KotlinNativeTarget>().forEach { target ->
            target.cinterop(this@createCommonizerTask, files, path, dependency)
        }
    }
}

private fun getAllFilesInFolderRecursive(
    folder: String,
    extension: String = "h"
): List<String> {
    val depsFolder = File(folder)
    val files = mutableListOf<String>()

    depsFolder.walk().forEach {
        val fileExtension = it.extension
        if (fileExtension == extension) {
            files.add(it.absolutePath)
        }
    }

    return files
}

private fun KotlinNativeTarget.cinterop(
    project: Project,
    files: List<String>,
    path: String,
    dependency: Dependency,
    groupName: String = "common",
    compilationName: String = "main",
) {
    val defName = dependency.name

    val srcPath = "src/${groupName}${compilationName.replaceFirstChar(Char::uppercase)}/cinterop"
    val srcPathFile = File(project.projectDir, srcPath).apply {
        createFolderIfNotExist()
    }
    val defFileName = "$defName.def"

    val defFile = File(srcPathFile, defFileName).apply {
        createFileIfNotExist()
    }

    val headersLine = "Headers: " + files.joinToString(" ") { it }
    val packageLine = "Package: $defName"
    defFile.writeText(headersLine + "\n" + packageLine)

    compilations.getByName(compilationName) {
        cinterops.maybeCreate(defName).defFile = defFile
    }
}
