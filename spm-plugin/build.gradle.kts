plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("kmp-spm") {
            id = "kotlin-multiplatform-spm"
            implementationClass = "com.programistich.spm.KotlinMultiplatformSwiftPackagePlugin"
        }
    }
}