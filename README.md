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
}
```
* Run ```gradlew createSPM``` what generate first SPM folder in build/spm
* After that, add local SPM to XCode project
* Copy path from build/spm/build.sh in comments for pre-build phase

After that, XCode auto rebuild spm(xcframework) before run app

### Macos
* If we configure MacOS in KotlinMultiplatformPlugin, we can add ```macos("10_15")``` and auto generate new platform in xcframework


### TODO
* Dependencies in SPM + Commonizer + check support ios/macos
* Graph Dependencies problem
* Generate workspace with prebuild phase