package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.deps.DependencyStorage
import com.programistich.spm.plugin.github.GithubApi
import com.programistich.spm.plugin.models.Dependency
import com.programistich.spm.plugin.models.GithubData
import com.programistich.spm.plugin.utils.SwiftPackageConstants
import com.programistich.spm.plugin.utils.createFolderIfNotExist
import com.programistich.spm.plugin.utils.unzip
import kotlinx.coroutines.runBlocking
import org.gradle.api.Project
import java.io.File

fun Project.downloadDependencies(dependencies: List<Dependency>) {
    val dependenciesFolder = File(project.buildDir, SwiftPackageConstants.DEPENDENCIES)
    dependenciesFolder.createFolderIfNotExist()

    dependencies.forEach { dependency ->

        val path: String = try {
            this.processDownload(dependency, dependenciesFolder)
        } catch (e: Exception) {
            getUnsafePath(dependency)
        }
        DependencyStorage.set(dependency, path)
    }
}

private fun Project.getUnsafePath(dependency: Dependency): String {
    return when (dependency) {
        is Dependency.Path -> dependency.path
        is Dependency.Github -> {
            val githubData = dependency.githubData
            val depsFolder = File(this.buildDir, SwiftPackageConstants.DEPENDENCIES)
            val depFolder = File(depsFolder, "${githubData.organization}/${githubData.repository}")

            return depFolder
                .listFiles()
                ?.first { it.isDirectory }
                ?.absolutePath ?: throw IllegalStateException("Dependency not found")
        }
    }
}

private fun Project.processDownload(
    dependency: Dependency,
    dependenciesFolder: File
): String = runBlocking {
    if (dependency is Dependency.Path) return@runBlocking File(dependency.path).absolutePath
    val githubDependency = checkNotNull(dependency as? Dependency.Github)

    val githubData = githubDependency.githubData

    val commit = when (githubDependency) {
        is Dependency.Github.Default -> {
            val branch = GithubApi.getDefaultBranch(githubData)
            GithubApi.getCommitByBranch(githubData, branch)
        }
        is Dependency.Github.Branch -> GithubApi.getCommitByBranch(githubData, githubDependency.branch)
        is Dependency.Github.Version -> GithubApi.getCommitByTag(githubData, githubDependency.version)
    }

    val dependencyPath = dependenciesFolder.absolutePath +
        "/" + githubData.organization +
        "/" + githubData.repository +
        "/" + commit
    val dependencyFolder = File(dependencyPath)

    processUnpack(dependencyFolder, githubData, commit)

    return@runBlocking dependencyFolder.absolutePath
}

private suspend fun Project.processUnpack(dependencyFolder: File, githubData: GithubData, commit: String) {
    if (dependencyFolder.exists()) {
        return
    } else {
        dependencyFolder.mkdirs()
    }

    val tmpPath = "${SwiftPackageConstants.DEPENDENCIES}/" +
        "${githubData.organization}-${githubData.repository}-$commit.zip"
    val tmpFile = File(project.buildDir, tmpPath)
    GithubApi.downloadByCommit(githubData, commit, tmpFile)
    tmpFile.unzip(dependencyFolder.absolutePath)
    tmpFile.delete()
}
