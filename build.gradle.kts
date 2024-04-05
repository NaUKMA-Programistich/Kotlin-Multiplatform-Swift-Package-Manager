plugins {
    id("com.android.library").version("8.2.0").apply(false)
    kotlin("multiplatform").version("1.9.22").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


