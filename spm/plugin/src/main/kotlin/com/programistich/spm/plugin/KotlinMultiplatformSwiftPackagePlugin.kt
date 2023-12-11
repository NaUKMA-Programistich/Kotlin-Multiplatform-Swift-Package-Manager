package com.programistich.spm.plugin

import com.programistich.spm.plugin.tasks.generateXCFramework
import com.programistich.spm.plugin.tasks.registerCreateSPM
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformSwiftPackagePlugin : Plugin<Project> {

    companion object {
        internal const val GRADLE_DSL = "swiftPackage"
        internal const val PREFIX_FRAMEWORK = "Framework"
        internal const val SPM_TASK_NAME = "createSPM"
        internal const val FOLDER = "spm"
        internal const val BUILD_FILE = "build.sh"
    }

    override fun apply(project: Project) {
        val spmExtension = project
            .extensions
            .create<KotlinMultiplatformSwiftPackageExtension>(
                name = GRADLE_DSL,
                constructionArguments = arrayOf(project)
            )

        project.afterEvaluate {
            project.extensions.findByType<KotlinMultiplatformExtension>()?.let { kmmExtension ->
                val frameworkName = spmExtension.packageName + PREFIX_FRAMEWORK
                project.generateXCFramework(kmmExtension, frameworkName)
                project.registerCreateSPM(spmExtension, frameworkName)
            }
        }
    }
}
