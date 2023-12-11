pluginManagement {
    includeBuild("spm")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Kotlin Multiplatform Swift Package Manager"
include(":shared")
include(":generator")
