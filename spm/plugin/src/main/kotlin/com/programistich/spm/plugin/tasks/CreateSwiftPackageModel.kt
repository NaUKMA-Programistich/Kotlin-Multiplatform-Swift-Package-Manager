package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackageExtension
import com.programistich.spm.plugin.deps.DependencyStorage
import com.programistich.spm.plugin.models.SwiftPackage
import com.programistich.spm.plugin.models.SwiftPackageDependency
import org.gradle.api.Project
import java.io.File

internal fun Project.createSwiftPackageModel(
    spmExtension: KotlinMultiplatformSwiftPackageExtension,
    frameworkName: String
): SwiftPackage {
    val buildType = getBuildType()
    val xcFrameworkTypeBuild = buildType.getPath()

    val xcFrameworkPath = "../XCFrameworks/$xcFrameworkTypeBuild/${frameworkName}.xcframework"

    val dependencies = DependencyStorage.getAllParser().map { (dependency, parser) ->
        SwiftPackageDependency(dependency, parser)
    }

    return SwiftPackage(
        packageName = checkNotNull(spmExtension.packageName),
        swiftVersion = checkNotNull(spmExtension.swiftVersion),
        iosVersion = checkNotNull(spmExtension.iosVersion),
        macosVersion = spmExtension.macosVersion,
        dependencies = dependencies,
        frameworkName = frameworkName,
        xcFrameworkPath = xcFrameworkPath
    )
}
