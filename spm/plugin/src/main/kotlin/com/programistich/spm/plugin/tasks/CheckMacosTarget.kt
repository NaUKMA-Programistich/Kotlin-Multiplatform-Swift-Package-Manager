package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.KotlinMultiplatformSwiftPackageExtension
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family

internal fun Project.checkMacosTarget(
    spmExtension: KotlinMultiplatformSwiftPackageExtension,
    kmmExtension: KotlinMultiplatformExtension
) {
    val targets = kmmExtension
        .targets
        .toList()
        .filterIsInstance<KotlinNativeTarget>()
        .map { it.konanTarget }
        .filter { it.family == Family.OSX }

    val isMacosTargetKMP = targets.isNotEmpty()
    val isMacosTargetSPM = spmExtension.macosVersion != null

    if (isMacosTargetSPM && !isMacosTargetKMP) {
        error("macos target is defined in SPM, but not defined in KMM")
    }
}
