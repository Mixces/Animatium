plugins {
    id("dev.kikugie.stonecutter")
    alias(libs.plugins.publishing)
    alias(libs.plugins.spotless)
}

stonecutter active "26.1-fabric" /* [SC] DO NOT EDIT */

stonecutter tasks {
    order("publishMods", versionComparator)
}

tasks.named("publishMods") {
    group = "build"
}

// Header
spotless {
    val licenseHeader = rootProject.file("HEADER")
    lineEndings = com.diffplug.spotless.LineEnding.UNIX

    java {
        licenseHeaderFile(licenseHeader)
        target("src/**/*.java", "versions/*/src/**/*.java")
    }

    kotlin {
        licenseHeaderFile(licenseHeader)
        target("src/**/*.kt", "versions/*/src/**/*.kt")
    }
}