package com.programistich.spm.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class KotlinMultiplatformSwiftPackagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val spmExtension = project
            .extensions
            .create<KotlinMultiplatformSwiftPackageExtension>(
                name = "spm",
                constructionArguments = arrayOf(project)
            )
    }
}
