plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-multiplatform-spm")
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosArm64()
    macosX64()

    sourceSets {
        @Suppress("UnusedPrivateProperty")
        val commonMain by getting {
            dependencies {
                implementation("co.touchlab:kermit:1.2.2")
            }
        }
//        val iosMain by creating
//        val iosX64Main by getting
//        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
//
//        iosMain.dependsOn(commonMain)
//        iosX64Main.dependsOn(iosMain)
//        iosArm64Main.dependsOn(iosMain)
//        iosSimulatorArm64Main.dependsOn(iosMain)
//
//        val macosMain by creating
//        val macosX64Main by getting
//        val macosArm64Main by getting
//
//        macosMain.dependsOn(commonMain)
//        macosX64Main.dependsOn(macosMain)
//        macosArm64Main.dependsOn(macosMain)
    }
}

android {
    namespace = "com.programistich.kmmspm"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}

swiftPackage {
    packageName("TestSPM")
    swift(5.3)
    ios("13")
    macos("10_15")

    dependencies {
        version("3.3.0", "https://github.com/airbnb/lottie-ios.git")
        url("https://github.com/facebook/facebook-ios-sdk")
        url("https://github.com/apple/swift-log")
        url("https://github.com/realm/realm-swift.git")
        branch("main", "https://github.com/gonzalezreal/swift-markdown-ui")
    }
}
