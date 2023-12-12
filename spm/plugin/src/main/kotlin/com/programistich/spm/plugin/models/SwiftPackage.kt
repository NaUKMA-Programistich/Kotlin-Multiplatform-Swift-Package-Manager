package com.programistich.spm.plugin.models

data class SwiftPackage(
    val packageName: String,
    val swiftVersion: Double,
    val iosVersion: String,
    val macosVersion: String?,
    val dependencies: List<Dependency>,
    val frameworkName: String,
    val buildType: BuildType,
    val xcFrameworkPath: String,
) {

    fun generateContent(): String {
        return """
// swift-tools-version:${swiftVersion}
import PackageDescription

let package = Package(
    name: "$packageName",
    platforms: [
        .iOS(.v${iosVersion}),
        ${if(macosVersion != null) ".macOS(.v${macosVersion})," else ""}
    ],
    products: [
        .library(
            name: "$packageName",
            targets: ["$packageName", "$frameworkName"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "$frameworkName",
            path: "$xcFrameworkPath"
        ),
        .target(
            name: "$packageName",
            dependencies: []
        ),
    ]
)""".trimIndent()
    }
}