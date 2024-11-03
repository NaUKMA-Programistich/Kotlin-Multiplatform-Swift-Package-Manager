package com.programistich.spm.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class KotlinMultiplatformSPMPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val spmExtension = project.getSPMExtensions()
        println(spmExtension)
    }
}

private fun Project.getSPMExtensions(): KotlinMultiplatformSPMExtension {
    return this
        .extensions
        .create<KotlinMultiplatformSPMExtension>(
            name = "spm",
            constructionArguments = arrayOf(project)
        )
}
