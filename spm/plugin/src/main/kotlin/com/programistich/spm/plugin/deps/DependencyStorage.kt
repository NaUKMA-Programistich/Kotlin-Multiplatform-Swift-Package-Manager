package com.programistich.spm.plugin.deps

import com.programistich.spm.plugin.models.Dependency
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias PathToDependency = String

object DependencyStorage {
    private val storage: HashMap<Dependency, PathToDependency> = hashMapOf()
    private var parsers: HashMap<Dependency, Parser> = hashMapOf()

    fun set(dependency: Dependency, path: PathToDependency) {
        storage[dependency] = path
    }

    fun getAll() = storage.toList()

    fun setParser(dependency: Dependency, parser: Parser) {
        parsers[dependency] = parser
    }

    fun getAllParser() = parsers.toList()
}

@Serializable
enum class DependencyCompatibilityType {
    @SerialName("exact")
    EXACT,

    @SerialName("range")
    RANGE,

    @SerialName("revision")
    REVISION,

    @SerialName("branch")
    BRANCH
}

@Serializable
data class Parser(
    val targets: List<TargetParser>,
    val products: List<ProductParser>,
    val commit: String,
    val name: String
)

@Serializable
data class ProductParser(val name: String)

@Serializable
data class TargetParser(val name: String, val dependencies: List<DependencyParser>)

@Serializable
data class DependencyCompatibility(
    val from: String? = null,
    val to: String? = null,
    val revision: String? = null,
    val branch: String? = null,
    val type: DependencyCompatibilityType
)

@Serializable
data class DependencyParser(
    val name: String,
    val compatibility: DependencyCompatibility? = null
)
