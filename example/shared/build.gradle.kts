plugins {
    id("kotlin-multiplatform-spm")
    kotlin("multiplatform").version("1.8.0")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
}
