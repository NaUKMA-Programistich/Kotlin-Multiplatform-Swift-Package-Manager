package com.programistich.spm.plugin.models

sealed interface Dependency {
    data class Default(val url: String): Dependency

    data class Version(val url: String, val version: String): Dependency
    data class Branch(val url: String, val branch: String): Dependency
    data class Path(val path: String): Dependency

    private fun getGithubData(url: String): Pair<String, String> {
        val splits = url.split("/")
        val size = splits.size

        val username = splits[size - 2]
        val repo = splits[size - 1].replace(".git", "")
        return username to repo
    }

    fun getGithubUrl(): String {
        return when (this) {
            is Default -> {
                val (username, repo) = getGithubData(url)
                "https://api.github.com/repos/$username/$repo/zipball"
            }

            is Branch -> {
                val (username, repo) = getGithubData(url)
                "https://api.github.com/repos/$username/$repo/zipball/$branch"
            }

            is Version -> {
                val (username, repo) = getGithubData(url)
                "https://api.github.com/repos/$username/$repo/zipball/$version"
            }

            is Path -> throw RuntimeException("Deps by path can not get github url")
        }
    }
}


class DependencyListBuilder {
    private val dependencies = mutableListOf<Dependency>()

    fun version(version: String, git: String) {
        val dependency: Dependency = Dependency.Version(git, version)
        dependencies.add(dependency)
    }

    fun branch(branch: String, git: String) {
        val dependency: Dependency = Dependency.Branch(git, branch)
        dependencies.add(dependency)
    }

    fun path(path: String) {
        val dependency: Dependency = Dependency.Path(path)
        dependencies.add(dependency)
    }

    fun url(path: String) {
        val dependency: Dependency = Dependency.Default(path)
        dependencies.add(dependency)
    }

    fun build(): List<Dependency> {
        return dependencies.toList()
    }
}
