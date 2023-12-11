plugins {
    id("com.android.library").version("8.1.1").apply(false)
    kotlin("multiplatform").version("1.9.10").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
