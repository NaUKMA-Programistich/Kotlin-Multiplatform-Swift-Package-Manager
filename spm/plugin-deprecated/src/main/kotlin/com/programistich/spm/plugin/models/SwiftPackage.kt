package com.programistich.spm.plugin.models

import com.programistich.spm.plugin.deps.Parser

data class SwiftPackage(
    val packageName: String,
    val swiftVersion: Double,
    val iosVersion: String,
    val macosVersion: String?,
    val dependencies: List<SwiftPackageDependency>,
    val frameworkName: String,
    val xcFrameworkPath: String,
) {
    fun generateContent(): String {
        return """
// swift-tools-version:$swiftVersion
import PackageDescription

let package = Package(
    name: "$packageName",
    platforms: [
        .iOS(.v$iosVersion),
        ${if (macosVersion != null) ".macOS(.v$macosVersion)," else ""}
    ],
    products: [
        .library(
            name: "$packageName",
            targets: ["$packageName", "$frameworkName"]
        ),
    ],
    dependencies: [
        ${dependencies.joinToString("\n\t\t") { it.generateGeneralContent()} }
    ],
    targets: [
        .binaryTarget(
            name: "$frameworkName",
            path: "$xcFrameworkPath"
        ),
        .target(
            name: "$packageName",
            dependencies: [
                ${dependencies.joinToString("\n\t\t\t\t") { it.generateProductContent()} }
            ]
        ),
    ]
)
        """.trimIndent()
    }

}

data class SwiftPackageDependency(
    val dependency: Dependency,
    val parser: Parser
) {
    fun generateGeneralContent(): String {
        return when (dependency) {
            is Dependency.Github.Branch -> ".package(url: \"${dependency.url}\", .branch(\"${dependency.branch}\")),"
            is Dependency.Github.Default -> ".package(url: \"${dependency.url}\", .revision(\"${parser.commit}\")),"
            is Dependency.Path -> ".package(path: \"${dependency.path}\"),"
            is Dependency.Github.Version -> ".package(url: \"${dependency.url}\", from: \"${dependency.version}\"),"
        }
    }

    fun generateProductContent(): String {
        val name = this.dependency.name
        return parser.products.joinToString("\n\t\t\t\t") { (product) ->
            ".product(name: \"$product\", package: \"${name}\"),"
        }
    }
}
