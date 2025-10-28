import dev.kikugie.stonecutter.data.tree.struct.ProjectNode

plugins {
    id("dev.kikugie.stonecutter")
    alias(libs.plugins.publishing)
    alias(libs.plugins.spotless)
}

stonecutter active "1.21.10-fabric" /* [SC] DO NOT EDIT */

stonecutter tasks {
    val ordering = Comparator
        .comparing<ProjectNode, _> { stonecutter.parse(it.metadata.version) }
        .thenComparingInt { if (it.metadata.project.endsWith("fabric")) 1 else 0 }
    order("publishMods", ordering)
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