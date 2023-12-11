package com.programistich.spm.plugin.models

data class Dependency(
    val git: String,
    val version: String,
    val `package`: String,
    val name: String,
) {
    fun generateContentDeps(): String {
        return ".package(url: \"$git\", from: \"$version\")"
    }

    fun generateContentTarget(): String {
        return ".product(name: \"$name\", package: \"$`package`\")"
    }

}

class DependencyListBuilder {
    private val dependencies = mutableListOf<Dependency>()

    fun dependency(git: String, version: String, `package`: String, name: String) {
        val dependency = Dependency(git, version, `package`, name)
        dependencies.add(dependency)
    }

    fun build(): List<Dependency> {
        return dependencies.toList()
    }
}
