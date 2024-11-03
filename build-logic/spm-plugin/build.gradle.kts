import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("kmp-spm-plugin") {
            id = "kotlin-multiplatform-spm"
            implementationClass = "com.programistich.spm.plugin.KotlinMultiplatformSPMPlugin"
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        allWarningsAsErrors = true

        freeCompilerArgs += listOf(
            "-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn"
        )
    }
}
