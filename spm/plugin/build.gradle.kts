import io.gitlab.arturbosch.detekt.Detekt

plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization").version("1.8.10")
    id("io.gitlab.arturbosch.detekt").version("1.23.4")
    id("maven-publish")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

gradlePlugin {
    plugins {
        create("kmm-spm") {
            id = "kotlin-multiplatform-spm"
            implementationClass = "com.programistich.spm.plugin.KotlinMultiplatformSwiftPackagePlugin"
        }
    }
}

tasks.register<Detekt>("detektFormat") {
    autoCorrect = true
}

tasks.withType<Detekt> {
    // Disable caching
    outputs.upToDateWhen { false }

    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
    }

    setSource(files(projectDir))
    config.setFrom(files("$rootDir/detekt.yml"))

    include("**/*.kt", "**/*.kts")
    exclude(
        "**/resources/**",
        "**/build/**",
    )

    parallel = true

    buildUponDefaultConfig = true

    allRules = true

    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                groupId = "com.programistich"
                artifactId = "spm-plugin"
                version = "0.0.1"
            }
        }
    }
}
