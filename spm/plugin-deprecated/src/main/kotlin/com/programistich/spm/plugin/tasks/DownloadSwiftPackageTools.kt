package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.utils.SwiftPackageConstants.SPM_PATH
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

internal fun Project.downloadSwiftPackageTools() {
    if (project.buildDir.resolve(SPM_PATH).exists()) {
        return
    }

    exec {
        workingDir = project.buildDir
        commandLine = listOf("git", "clone", "https://github.com/apple/${SPM_PATH}")
        standardOutput = ByteArrayOutputStream()
    }
}
