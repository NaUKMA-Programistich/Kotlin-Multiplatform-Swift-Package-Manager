plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

    implementation("io.ktor:ktor-client-core:2.3.6")
    implementation("io.ktor:ktor-client-cio:2.3.6")
}

gradlePlugin {
    plugins {
        create("kmm-spm") {
            id = "kotlin-multiplatform-spm"
            implementationClass = "com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin"
        }
    }
}