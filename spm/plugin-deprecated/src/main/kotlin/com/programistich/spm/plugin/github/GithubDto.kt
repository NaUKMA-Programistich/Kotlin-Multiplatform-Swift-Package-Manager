package com.programistich.spm.plugin.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubDefaultDto(
    @SerialName("default_branch")
    val defaultBranch: String,
)

@Serializable
data class GithubBranchDto(
    @SerialName("name")
    val name: String,
    @SerialName("commit")
    val commit: GithubBranchCommitDto,
)

@Serializable
data class GithubBranchCommitDto(
    @SerialName("sha")
    val sha: String,
)

@Serializable
data class GithubTagDto(
    @SerialName("name")
    val name: String,
    @SerialName("commit")
    val commit: GithubTagCommitDto,
)

@Serializable
data class GithubTagCommitDto(
    @SerialName("sha")
    val sha: String,
)
