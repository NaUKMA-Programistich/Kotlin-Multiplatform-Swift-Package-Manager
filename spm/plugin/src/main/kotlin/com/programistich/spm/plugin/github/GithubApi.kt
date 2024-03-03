package com.programistich.spm.plugin.github

import com.programistich.spm.plugin.models.GithubData
import com.programistich.spm.plugin.utils.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File

object GithubApi {
    suspend fun getDefaultBranch(githubData: GithubData): String {
        val url = "https://api.github.com/repos/${githubData.organization}/${githubData.repository}"
        val response: GithubDefaultDto = httpClient.get(url).body()
        return response.defaultBranch
    }

    suspend fun getCommitByBranch(githubData: GithubData, branch: String): String {
        val url = "https://api.github.com/repos/${githubData.organization}/${githubData.repository}/branches/$branch"
        val response: GithubBranchDto = httpClient.get(url).body()
        return response.commit.sha
    }

    suspend fun getCommitByTag(githubData: GithubData, tag: String): String {
        val url = "https://api.github.com/repos/${githubData.organization}/${githubData.repository}/tags"
        val response: List<GithubTagDto> = httpClient.get(url).body()
        return response.first { it.name == tag }.commit.sha
    }

    suspend fun downloadByCommit(githubData: GithubData, commit: String, file: File) {
        val url = "https://api.github.com/repos/${githubData.organization}/${githubData.repository}/zipball/$commit"
        val response = httpClient.get(url).bodyAsChannel()
        response.copyAndClose(file.writeChannel())
    }
}
