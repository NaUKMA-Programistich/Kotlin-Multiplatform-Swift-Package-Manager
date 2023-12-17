package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackageExtension
import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin
import com.programistich.spm.plugin.deps.ProcessDependencies
import com.programistich.spm.plugin.models.BuildType
import com.programistich.spm.plugin.models.SwiftPackage
import com.programistich.spm.plugin.models.SwiftPackageException
import org.gradle.api.Project

private const val PROP_TYPE_RELEASE = "buildType"

internal fun Project.registerCreateSPM(
    spmExtension: KotlinMultiplatformSwiftPackageExtension, frameworkName: String
) {
    val project = this
    tasks.register("createSPM") {
        description = "Create Swift Package for Kotlin Multiplatform Mobile"
        when (project.getBuildType()) {
            BuildType.DEBUG -> dependsOn("assemble${frameworkName}DebugXCFramework")
            BuildType.RELEASE -> dependsOn("assemble${frameworkName}ReleaseXCFramework")
        }

        doLast {
            val swiftPackage = createSPM(spmExtension)

            project.generateSPMBuildFolder(swiftPackage)
            project.generatePreBuildScript(swiftPackage)
            project.validateSwiftPackage(swiftPackage)
            project.downloadDependencies(swiftPackage)
        }
    }
}

private fun Project.createSPM(spmExtension: KotlinMultiplatformSwiftPackageExtension): SwiftPackage {
    val buildType = this.getBuildType()

    val packageName = spmExtension.packageName ?: throw SwiftPackageException("Package name is null")
    val swiftVersion = spmExtension.swiftVersion ?: throw SwiftPackageException("Swift version is null")
    val iosVersion = spmExtension.iosVersion ?: throw SwiftPackageException("iOS version is null")
    val dependencies = spmExtension.dependencies
    val frameworkName = packageName + KotlinMultiplatformSwiftPackagePlugin.PREFIX_FRAMEWORK

    val xcFrameworkTypeBuild = buildType.getPath()
    val xcFrameworkPath = "../XCFrameworks/$xcFrameworkTypeBuild/${frameworkName}.xcframework"

    return SwiftPackage(
        packageName = packageName,
        swiftVersion = swiftVersion,
        iosVersion = iosVersion,
        dependencies = dependencies,
        frameworkName = frameworkName,
        buildType = buildType,
        xcFrameworkPath = xcFrameworkPath,
        macosVersion = spmExtension.macosVersion
    )
}

internal fun Project.getBuildType(): BuildType {
    val rawBuildType: String? = project.findProperty(PROP_TYPE_RELEASE) as String?
    return BuildType.fromString(rawBuildType) ?: BuildType.RELEASE
}
