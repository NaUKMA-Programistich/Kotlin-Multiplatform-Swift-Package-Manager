package com.programistich.spm.plugin.deps

import com.programistich.spm.plugin.models.Dependency
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.runBlocking
import java.io.File

object ProcessDependencies {
    private val client = HttpClient(CIO)

    fun getSourceFolder(dependency: Dependency, dependenciesFolder: File): File = runBlocking {
        return@runBlocking when (dependency) {
            is Dependency.Path -> File(dependency.path)
            else -> processDownloadFromGithub(dependenciesFolder, dependency)
        }
    }

    private suspend fun processDownloadFromGithub(dependenciesFolder: File, dependency: Dependency): File {
        val zipFile = File(dependenciesFolder, "${dependency.hashCode()}.zip")
        val githubUrl = dependency.getGithubUrl()

        val response = client.get(githubUrl).bodyAsChannel()
        response.copyAndClose(zipFile.writeChannel())

        UnzipUtils.unzip(zipFile, dependenciesFolder.absolutePath)

        zipFile.deleteRecursively()
        return dependenciesFolder.listFiles()?.first { it.isDirectory } ?: throw RuntimeException("")
    }
}
