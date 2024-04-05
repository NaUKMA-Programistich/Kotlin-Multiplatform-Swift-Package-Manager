package com.programistich.spm.plugin

import com.programistich.spm.plugin.tasks.generateXCFramework
import com.programistich.spm.plugin.tasks.registerCreateSPM
import com.programistich.spm.plugin.utils.SwiftPackageConstants
import com.programistich.spm.plugin.utils.SwiftPackageConstants.PREFIX_FRAMEWORK
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformSwiftPackagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val spmExtension = project
            .extensions
            .create<KotlinMultiplatformSwiftPackageExtension>(
                name = SwiftPackageConstants.GRADLE_DSL,
                constructionArguments = arrayOf(project)
            )

        project.afterEvaluate {
            val kmmExtension = project.findKotlinMultiplatformExtension() ?: return@afterEvaluate
            val frameworkName = spmExtension.packageName + PREFIX_FRAMEWORK
            project.generateXCFramework(kmmExtension, frameworkName)
            project.registerCreateSPM(spmExtension, frameworkName, kmmExtension)
        }
    }
}

internal fun Project.findKotlinMultiplatformExtension(): KotlinMultiplatformExtension? {
    return project.extensions.findByType(KotlinMultiplatformExtension::class.java)
}
