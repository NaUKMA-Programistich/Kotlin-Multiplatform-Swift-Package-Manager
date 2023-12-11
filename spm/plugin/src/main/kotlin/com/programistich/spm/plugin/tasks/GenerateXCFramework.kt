package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.models.SwiftPackage
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkConfig


internal fun Project.generateXCFramework(
    kmmExtension: KotlinMultiplatformExtension,
    frameworkName: String,
) {
    val xcFrameworkConfig = XCFrameworkConfig(this, frameworkName)

    val targets = kmmExtension
        .targets
        .toList()
        .filterIsInstance<KotlinNativeTarget>()

    kmmExtension.apply {
        targets.forEach {
            it.binaries.framework {
                baseName = frameworkName
                xcFrameworkConfig.add(this)
            }
        }
    }
}