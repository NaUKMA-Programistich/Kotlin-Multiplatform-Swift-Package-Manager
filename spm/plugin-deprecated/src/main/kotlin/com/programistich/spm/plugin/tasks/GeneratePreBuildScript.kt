package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.utils.SwiftPackageConstants
import org.gradle.api.Project
import java.io.File

internal fun Project.generatePreBuildScript() {
    val spmBuildFolder = File(project.buildDir, SwiftPackageConstants.FOLDER)
    if (!spmBuildFolder.exists()) {
        spmBuildFolder.mkdirs()
    }

    val spmScriptFile = File(spmBuildFolder, SwiftPackageConstants.BUILD_FILE)
    if (!spmScriptFile.exists()) {
        spmScriptFile.createNewFile()
    }

    val rootFile = project.rootDir
    val rootFilePath = rootFile.absolutePath

    val subProjectFile = project.projectDir
    val subProjectFilePath = subProjectFile.absolutePath

    val diffPath = subProjectFilePath.replace(rootFilePath, "")
    val subprojectPath = diffPath.replace("/", ":")

    val content = generateContent(
        spmBuildPath = spmScriptFile.absolutePath.replace(" ", "\\ "),
        rootFilePath = rootFilePath.replace(" ", "\\ "),
        subproject = subprojectPath
    )

    spmScriptFile.writeText(content)
}

private fun generateContent(
    spmBuildPath: String,
    rootFilePath: String,
    subproject: String
): String {
    val rootGradleSh = "$" + "ROOT_GRADLE"
    val subprojectSh = "$" + "SUBPROJECT"
    val gradleTaskSh = "$" + "GRADLE_TASK"
    val configuration = "$" + "CONFIGURATION"

    return """
        #!/usr/bin/env sh
        
        # Add this script in pre build phase
        # chmod +x $spmBuildPath && $spmBuildPath
        
        set -ev
        
        export SUBPROJECT="$subproject"
        export GRADLE_TASK="$gradleTaskSh"
        
        cd $rootFilePath
        
        ./gradlew $rootGradleSh $subprojectSh:createSPM -PbuildType=$configuration
    """.trimIndent()
}
