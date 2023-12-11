package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackageExtension
import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin
import com.programistich.spm.plugin.models.BuildType
import com.programistich.spm.plugin.models.SwiftPackage
import org.gradle.api.Project

private const val PROP_TYPE_RELEASE = "buildType"

internal fun Project.registerCreateSPM(
    spmExtension: KotlinMultiplatformSwiftPackageExtension, frameworkName: String
) {
    val project = this
    tasks.register("createSPM") {
        description = "Create Swift Package for Kotlin Multiplatform Mobile"

        val buildType = project.getBuildType()
        when (buildType) {
            BuildType.DEBUG -> dependsOn("assemble${frameworkName}DebugXCFramework")
            BuildType.RELEASE -> dependsOn("assemble${frameworkName}ReleaseXCFramework")
        }


        doLast {
            val swiftPackage = createSPM(spmExtension)

            project.generateSPMBuildFolder(swiftPackage)
            project.generatePreBuildScript(swiftPackage)
        }
    }
}

private fun Project.createSPM(spmExtension: KotlinMultiplatformSwiftPackageExtension): SwiftPackage {
    val buildType = this.getBuildType()

    val packageName = spmExtension.packageName ?: throw Exception("Package name is null")
    val swiftVersion = spmExtension.swiftVersion ?: throw Exception("Swift version is null")
    val iosVersion = spmExtension.iosVersion ?: throw Exception("iOs version is null")
    val dependencies = spmExtension.dependencies

    val xcFrameworkTypeBuild = buildType.getPath()
    val xcFrameworkPath = "../XCFrameworks/$xcFrameworkTypeBuild/TestSPMFramework.xcframework"

    return SwiftPackage(
        packageName = packageName,
        swiftVersion = swiftVersion,
        iosVersion = iosVersion,
        dependencies = dependencies,
        frameworkName = packageName + KotlinMultiplatformSwiftPackagePlugin.SPM_TASK_NAME,
        buildType = buildType,
        xcFrameworkPath = xcFrameworkPath,
        macosVersion = spmExtension.macosVersion
    )
}

internal fun Project.getBuildType(): BuildType {
    val rawBuildType: String? = project.findProperty(PROP_TYPE_RELEASE) as String?
    return BuildType.fromString(rawBuildType) ?: BuildType.RELEASE
}
