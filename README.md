# Kotlin Multiplatform Plugin for use Swift Package Manager

### How to use
* Dependencies for plugin ```TODO```
* Connect id("kotlin-multiplatform-spm") in build.gradle.kts
* Configuration in root
```kotlin
swiftPackage {
    packageName("TestSPM")
    swift(5.3)
    ios("13")

    dependencies {
        version("3.3.0", "https://github.com/airbnb/lottie-ios.git")
        url("https://github.com/facebook/facebook-ios-sdk")
        url("https://github.com/apple/swift-log")
        url("https://github.com/realm/realm-swift.git")
        branch("main", "https://github.com/gonzalezreal/swift-markdown-ui")
    }
}
```
* Run ```gradlew createSPM``` what generate first SPM folder in build/spm
* After that, add local SPM to XCode project
* Copy path from build/spm/build.sh in comments for pre-build phase

After that, XCode auto rebuild spm(xcframework) before run app

* In Xcode code we can use TestSPMFramework

### Macos
* If we configure MacOS in KotlinMultiplatformPlugin, we can add ```macos("10_15")``` and auto generate new platform in xcframework


### TODO
* Commonizer
* Graph Dependencies problem
