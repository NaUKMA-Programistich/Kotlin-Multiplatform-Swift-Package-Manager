package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackageExtension
import com.programistich.spm.plugin.models.BuildType
import com.programistich.spm.plugin.utils.SwiftPackageConstants
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.registerCreateSPM(
    spmExtension: KotlinMultiplatformSwiftPackageExtension,
    frameworkName: String,
    kmmExtension: KotlinMultiplatformExtension
) {
    val project = this
    tasks.register("createSPM") {
        description = "Create Swift Package for Kotlin Multiplatform Mobile"
        when (project.getBuildType()) {
            BuildType.DEBUG -> dependsOn("assemble${frameworkName}DebugXCFramework")
            BuildType.RELEASE -> dependsOn("assemble${frameworkName}ReleaseXCFramework")
        }

        doLast {
            project.checkMacosTarget(spmExtension, kmmExtension)

            // SPM Tools
            project.downloadSwiftPackageTools()
            project.generateSwiftPackageToolsParser()

            // Work with dependencies
            project.downloadDependencies(dependencies = spmExtension.dependencies)
            project.parseDependencies()

            val swiftPackage = project.createSwiftPackageModel(spmExtension, frameworkName)
            project.generateSPMBuildFolder(swiftPackage)
            project.generatePreBuildScript()

            // Validate Swift Package
            project.validateSwiftPackage()

            // project.createCommonizerTask()
        }
    }
}

internal fun Project.getBuildType(): BuildType {
    val rawBuildType: String? = project.findProperty(SwiftPackageConstants.PROP_TYPE_BUILD) as String?
    return BuildType.fromString(rawBuildType) ?: BuildType.RELEASE
}
