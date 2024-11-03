package com.programistich.spm.plugin

import com.programistich.spm.plugin.models.Dependency
import com.programistich.spm.plugin.models.DependencyListBuilder
import org.gradle.api.Project

open class KotlinMultiplatformSwiftPackageExtension(private val project: Project) {
    internal var packageName: String? = null
    internal var swiftVersion: Double? = null
    internal var iosVersion: String? = null
    internal var macosVersion: String? = null

    internal var dependencies: List<Dependency> = emptyList()

    fun packageName(packageName: String) {
        this.packageName = packageName
    }

    fun swift(version: Double) {
        this.swiftVersion = version
    }

    fun dependencies(block: DependencyListBuilder.() -> Unit) {
        this.dependencies = DependencyListBuilder().apply(block).build()
    }

    fun ios(version: String) {
        this.iosVersion = version
    }

    fun macos(version: String) {
        this.macosVersion = version
    }
    override fun toString(): String {
        return "(packageName=$packageName, swiftVersion=$swiftVersion," +
            "iosVersion=$iosVersion, dependencies=$dependencies)"
    }
}
