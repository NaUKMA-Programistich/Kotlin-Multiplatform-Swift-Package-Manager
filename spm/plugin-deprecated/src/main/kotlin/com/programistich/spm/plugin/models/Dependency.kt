package com.programistich.spm.plugin.models

import java.net.URL

sealed interface Dependency {
    sealed class Github(private val url: String) {
        data class Default(val url: String) : Dependency, Github(url)
        data class Version(val url: String, val version: String) : Dependency, Github(url)
        data class Branch(val url: String, val branch: String) : Dependency, Github(url)

        val githubData by lazy {
            val url = URL(this.url)
            val parts = url.path.split("/")
            val organization = parts[1]
            val repository = parts[2].replace(".git", "")
            GithubData(organization, repository)
        }
    }

    data class Path(val path: String) : Dependency

    val name: String
        get() = when (this) {
            is Github -> this.githubData.repository.substringAfterLast("/")
            is Path -> this.path.substringAfterLast("/")
        }
}

data class GithubData(
    val organization: String,
    val repository: String,
)

class DependencyListBuilder {
    private val dependencies = mutableListOf<Dependency>()

    fun version(version: String, git: String) {
        val dependency: Dependency = Dependency.Github.Version(git, version)
        dependencies.add(dependency)
    }

    fun branch(branch: String, git: String) {
        val dependency: Dependency = Dependency.Github.Branch(git, branch)
        dependencies.add(dependency)
    }

    fun path(path: String) {
        val dependency: Dependency = Dependency.Path(path)
        dependencies.add(dependency)
    }

    fun url(path: String) {
        val dependency: Dependency = Dependency.Github.Default(path)
        dependencies.add(dependency)
    }

    fun build(): List<Dependency> {
        return dependencies.toList()
    }
}
