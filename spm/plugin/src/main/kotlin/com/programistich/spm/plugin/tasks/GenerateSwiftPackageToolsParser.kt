package com.programistich.spm.plugin.tasks

import com.programistich.spm.plugin.utils.SwiftPackageConstants
import com.programistich.spm.plugin.utils.SwiftPackageConstants.SPM_TOOLS_PATH
import com.programistich.spm.plugin.utils.createFileIfNotExist
import com.programistich.spm.plugin.utils.createFolderIfNotExist
import org.gradle.api.Project
import java.io.File

internal fun Project.generateSwiftPackageToolsParser() {
    // Folder for swift-package-tools
    val folder = File(project.buildDir, SPM_TOOLS_PATH).apply { createFolderIfNotExist() }

    // Folder Sources for swift-package-tools
    val sourceFolder = File(folder, "Sources").apply { createFolderIfNotExist() }

    val swiftPackageToolsFolder = File(sourceFolder, "swift-package-tools").apply { createFolderIfNotExist() }

    val spmSource = File(project.buildDir, SwiftPackageConstants.SPM_PATH)

    // File Package.swift for swift-package-tools
    File(folder, "Package.swift").apply {
        createFileIfNotExist()
        writeText(generateContentForSwiftPackage(spmSource.absolutePath))
    }

    File(swiftPackageToolsFolder, "parser.swift").apply {
        createFileIfNotExist()
        writeText(generateContentForSwiftPackageMain())
    }
}

fun generateContentForSwiftPackageMain(): String {
    return """
import Basics
import Foundation
import Workspace
import Logging

private let FROM_ARG = "--from="
private let TO_ARG = "--to="

let logger = Logger(label: "SwiftPackageTools")

@main
@available(macOS 13, iOS 15, tvOS 15, watchOS 8, *)
struct SwiftPackageTools {
    static func main() async throws {
        let arguments = CommandLine.arguments
        logger.info("Arguments \(arguments)")
        
        guard
            let fromArg = arguments
                .first(where: { ${'$'}0.starts(with: FROM_ARG) })?
                .replacing(FROM_ARG, with: ""),
            let toArg = arguments
                .first(where: { ${'$'}0.starts(with: TO_ARG) })?
                .replacing(TO_ARG, with: ""),
            let packagePath = try? AbsolutePath(validating: fromArg)
        else { return }
        let jsonPath = URL(fileURLWithPath: toArg)
        
        logger.info("SPM path \(packagePath)")
        logger.info("JSON path \(jsonPath)")
        
        var parserTargets: [TargetParser] = []
        var parserProducts: [ProductParser] = []
        
        let observability = ObservabilitySystem({ _, _ in })
        let workspace = try Workspace(forRootPackage: packagePath)
        let manifest = try await workspace.loadRootManifest(at: packagePath, observabilityScope: observability.topScope)
        let rootPackage = try await workspace.loadRootPackage(at: packagePath, observabilityScope: observability.topScope)
        let graph = try workspace.loadPackageGraph(rootPath: packagePath, observabilityScope: observability.topScope)
        
        let targets = manifest.targets
        let commit = rootPackage.identity.description
        let depsName = manifest.displayName
        
        for target in targets.filter({ !${'$'}0.isTest }) {
            var deps: [DependencyParser] = []
            
            for dependency in target.dependencies {
                switch dependency {
                case .target(name: let name, condition: _):
                    deps.append(DependencyParser(name: name))
                case .product(name: let name, package: let package, moduleAliases: _, condition: _):
                    guard let depsIdentity = graph.requiredDependencies.first(where: { ${'$'}0.deprecatedName == package })?.identity else {
                        deps.append(DependencyParser(name: name))
                        continue
                    }
                    guard let depsManifest = manifest.dependencies.first(where: { ${'$'}0.identity == depsIdentity }) else {
                        deps.append(DependencyParser(name: name))
                        continue
                    }
                    
                    switch(depsManifest) {
                        
                    case .fileSystem(_):
                        deps.append(DependencyParser(name: name))
                        continue
                    case .registry(_):
                        deps.append(DependencyParser(name: name))
                        continue
                    case .sourceControl(let control):
                        var compatibility: DependencyCompatibility
                        
                        switch(control.requirement) {
                        case .exact(let exect):
                            let from = "\(exect.major).\(exect.minor).\(exect.patch)"
                            compatibility = DependencyCompatibility.createExect(from: from)
                        case .range(let range):
                            let from = "\(range.lowerBound.major).\(range.lowerBound.minor).\(range.lowerBound.patch)"
                            let to = "\(range.upperBound.major).\(range.upperBound.minor).\(range.upperBound.patch)"
                            compatibility = DependencyCompatibility.createRange(from: from, to: to)
                        case .revision(let revision):
                            compatibility = DependencyCompatibility.createRevision(revision: revision)
                        case .branch(let branch):
                            compatibility = DependencyCompatibility.createBranch(branch: branch)
                        }
                        deps.append(DependencyParser(name: name, compatibility: compatibility))
                    }

                case .byName(name: let name, condition: _):
                    deps.append(DependencyParser(name: name))
                }
            }
            parserTargets.append(TargetParser(name: target.name, dependencies: deps))
        }
        
        for product in rootPackage.products.filter({ ${'$'}0.type != .test }) {
            parserProducts.append(ProductParser(name: product.name))
        }
        
        let parser = Parser(products: parserProducts, targets: parserTargets, commit: commit, name: depsName)
        
        do {
            let encoder = JSONEncoder()
            encoder.outputFormatting = .prettyPrinted
            let data = try encoder.encode(parser)
            
            try data.write(to: jsonPath, options: [.atomicWrite])
            
            logger.info("Success")
        } catch {
            logger.error("Error on write to json \(error.localizedDescription)")
        }
    }
}

struct Parser: Codable {
    let products: [ProductParser]
    let targets: [TargetParser]
    let commit: String
    let name: String
}

struct TargetParser: Codable {
    let name: String
    let dependencies: [DependencyParser]
}

struct ProductParser: Codable {
    let name: String
}

struct DependencyParser: Codable {
    let name: String
    let compatibility: DependencyCompatibility?
    
    init(name: String) {
        self.name = name
        self.compatibility = nil
    }
    
    init(name: String, compatibility: DependencyCompatibility) {
        self.compatibility = compatibility
        self.name = name
    }
}

struct DependencyCompatibility: Codable {
    let from: String?
    let to: String?
    
    let branch: String?

    let revision: String?
    
    let type: DependencyCompatibilityType
    
    static func createExect(from: String) -> Self {
        return DependencyCompatibility(from: from, to: nil, branch: nil, revision: nil, type: .exact)
    }
    
    static func createRange(
        from: String,
        to: String
    ) -> Self {
        return DependencyCompatibility(from: from, to: to, branch: nil, revision: nil, type: .range)
    }
    
    static func createRevision(revision: String) -> Self {
        return DependencyCompatibility(from: nil, to: nil, branch: nil, revision: revision, type: .revision)
    }
    
    static func createBranch(branch: String) -> Self {
        return DependencyCompatibility(from: nil, to: nil, branch: branch, revision: nil, type: .branch)
    }
    
}

enum DependencyCompatibilityType: String, Codable {
    case exact
    case range
    case revision
    case branch
}

struct SemVer: Codable {
    let major: Int
    let minor: Int
    let patch: Int
}
 """.trimIndent()
}

private fun generateContentForSwiftPackage(path: String): String {
    return """
// swift-tools-version:5.7

import PackageDescription

let package = Package(
    name: "swift-package-tools",
    platforms: [ .macOS(.v13) ],
    dependencies: [
        .package(name: "swift-package-manager", path: "$path"),
        .package(url: "https://github.com/apple/swift-log.git", from: "1.0.0"),
    ],
    targets: [
        .executableTarget(
            name: "swift-package-tools",
            dependencies: [
                .product(name: "SwiftPM", package: "swift-package-manager"),
                .product(name: "Logging", package: "swift-log")
            ]
        ),
    ]
)
    """.trimIndent()
}
