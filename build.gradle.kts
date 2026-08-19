@file:OptIn(StonecutterExperimentalAPI::class)

import com.google.devtools.ksp.processing.parseBoolean
import dev.kikugie.stonecutter.StonecutterExperimentalAPI
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.fabricapi.FabricApiExtension

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.loom.remap) apply false
    alias(libs.plugins.publishing)
    alias(libs.plugins.blossom)
    alias(libs.plugins.ksp)
    alias(libs.plugins.fletchingtable.fabric)
}

repositories {
    maven("https://maven.parchmentmc.org") // Parchment
    maven("https://maven.nucleoid.xyz/") // Placeholder API - required by Mod Menu
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://maven.bawnorton.com/releases") // MixinSquared
    maven("https://maven.terraformersmc.com/") // Mod Menu
    maven("https://maven.isxander.dev/releases") // YACL
}

class ModData {
    val id = property("mod.id") as String
    val name = property("mod.name") as String
    val version = property("mod.version") as String
    val group = property("mod.group") as String
    val description = property("mod.description") as String
    val source = property("mod.source") as String
    val issues = property("mod.issues") as String
    val license = property("mod.license") as String
    val modrinth = property("mod.modrinth") as String
    val curseforge = property("mod.curseforge") as String
    val discord = property("mod.discord") as String
    val obfuscated = parseBoolean(property("mod.obfuscated") as String)
    val development = parseBoolean(property("mod.development") as String)

    val minecraftVersion = property("mod.minecraft_version") as String
    val minecraftVersionRange = property("mod.minecraft_version_range") as String
}

class Dependencies {
    val fabricLoaderVersion = property("deps.fabric_loader_version") as String?
    val fabricApiVersion = property("deps.fabric_api_version") as String?
    val languageKotlinVersion = property("deps.language_kotlin_version") as String?
    val devAuthVersion = property("deps.devauth_version") as String?
    val mixinConstraintsVersion = property("deps.mixinconstraints_version") as String?
    val mixinSquaredVersion = property("deps.mixinsquared_version") as String?
}

val mod = ModData()
val deps = Dependencies()

// Apply specific loom
if (mod.obfuscated) {
    apply(plugin = "net.fabricmc.fabric-loom-remap")
} else {
    apply(plugin = "net.fabricmc.fabric-loom")
}

class LoaderData {
    val name = property("loader.platform") as String?
}

val loader = LoaderData()

version = "${mod.version}+${mod.minecraftVersion}-${loader.name}" + (if (mod.development) "_development" else "")
group = mod.group
base { archivesName.set(mod.id) }

extensions.configure<LoomGradleExtensionAPI> {
    runConfigs.remove(runConfigs["server"]) // Removes server run configs
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }

    accessWidenerPath = stonecutter.process(
        rootProject.file("src/main/resources/${mod.id}.accesswidener"),
        "build/processed.accesswidener"
    )

    runs {
        afterEvaluate {
            configureEach {
                property("mixin.hotSwap", "true")
                property("mixin.debug.export", "true") // Puts mixin outputs in /run/.mixin.out
                property("devauth.enabled", "true")
                property("devauth.account", "main")
            }
        }
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "${mod.id}.mixins.json")
    }

    lang.create("main") {
        patterns.add("assets/${mod.id}/lang/**")
    }
}

val loom: LoomGradleExtensionAPI by extensions
val fabricApi: FabricApiExtension by extensions
val minecraft by configurations.existing
val include by configurations.existing
val modImplementation: NamedDomainObjectProvider<Configuration> =
    configurations.named(if (mod.obfuscated) "modImplementation" else "implementation")
val modRuntimeOnly: NamedDomainObjectProvider<Configuration> =
    configurations.named(if (mod.obfuscated) "modRuntimeOnly" else "runtimeOnly")

dependencies {
    minecraft("com.mojang:minecraft:${mod.minecraftVersion}")

    if (mod.obfuscated) {
        val mappings by configurations.existing

        @Suppress("UnstableApiUsage")
        mappings(loom.layered {
            officialMojangMappings()

            optionalProp("deps.parchment_version") {
                parchment("org.parchmentmc.data:parchment-${mod.minecraftVersion}:$it@zip")
            }
        })
    }

    modRuntimeOnly("me.djtheredstoner:DevAuth-${loader.name}:${deps.devAuthVersion}")
    include(implementation("com.moulberry:mixinconstraints:${deps.mixinConstraintsVersion}")!!)
    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-${loader.name}:${deps.mixinSquaredVersion}")!!)!!)

    modImplementation("net.fabricmc:fabric-loader:${deps.fabricLoaderVersion}")!!
    modImplementation("net.fabricmc.fabric-api:fabric-api:${deps.fabricApiVersion}")
    optionalProp("deps.modmenu_version") { prop ->
        modImplementation("com.terraformersmc:modmenu:$prop")
    }

    include(implementation("net.fabricmc:fabric-language-kotlin:${deps.languageKotlinVersion}")!!)
    optionalProp("deps.yacl_version") { prop ->
        modImplementation("dev.isxander:yet-another-config-lib:$prop")
    }
}

val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }
val curseforgeId = findProperty("publish.curseforge")?.toString()?.takeIf { it.isNotBlank() }

// accessTokens should be placed in the user Gradle gradle.properties file
// for example, on Windows this would be "C:\Users\{user}\.gradle\gradle.properties"
// then add:
// modrinth.token=
// curseforge.token=
publishMods {
    file =
        (if (mod.obfuscated) tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") else tasks.jar).flatMap { it.archiveFile }

    val niceVersionRangeTitle = if (mod.minecraftVersionRange.contains(' ')) {
        val parts = mod.minecraftVersionRange.trim().split(' ')
        parts.first() + '-' + parts.last()
    } else {
        mod.minecraftVersionRange
    }

    displayName = "Release ${mod.version} for $niceVersionRangeTitle"
    version = mod.version
    changelog = project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = STABLE

    modLoaders.add(loader.name ?: "fabric")

    dryRun = modrinthId == null && curseforgeId == null
    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = findProperty("modrinth.token").toString()
            minecraftVersions.addAll(mod.minecraftVersionRange.split(' '))

            requires("fabric-api")
            requires("yacl")
            optional("modmenu")
        }
    }

    if (curseforgeId != null) {
        curseforge {
            projectId = curseforgeId
            projectSlug = mod.id
            accessToken = findProperty("curseforge.token").toString()
            minecraftVersions.addAll(mod.minecraftVersionRange.split(' '))
            client = true

            requires("fabric-api")
            requires("yacl")
            optional("modmenu")
        }
    }

    val discordWebhookUrl = findProperty("discord.webhook")?.toString()?.takeIf { it.isNotBlank() }
    if (discordWebhookUrl != null) {
        discord {
            webhookUrl = discordWebhookUrl
        }
    }
}

java {
    val requiredJava = when {
        stonecutter.eval(stonecutter.current.version, ">=26.1") -> JavaVersion.VERSION_25
        stonecutter.eval(stonecutter.current.version, ">=1.20.5") -> JavaVersion.VERSION_21
        stonecutter.eval(stonecutter.current.version, ">=1.18") -> JavaVersion.VERSION_17
        stonecutter.eval(stonecutter.current.version, ">=1.17") -> JavaVersion.VERSION_16
        else -> JavaVersion.VERSION_1_8
    }

    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
    if (!mod.obfuscated) {
        withSourcesJar()
    }
}

tasks {
    processResources {
        val props = buildMap {
            put("id", mod.id)
            put("name", mod.name)
            put("version", mod.version)
            put("description", mod.description)
            put("source", mod.source)
            put("issues", mod.issues)
            put("license", mod.license)
            put("modrinth", mod.modrinth)
            put("curseforge", mod.curseforge)
            put("discord", mod.discord)
            put("fabric_loader_version", deps.fabricLoaderVersion)

            val minecraftVersionRange = if (mod.minecraftVersionRange.contains(' ')) {
                val parts = mod.minecraftVersionRange.trim().split(' ')
                ">=" + parts.first() + ' ' + "<=" + parts.last()
            } else {
                mod.minecraftVersionRange
            }

            put("minecraft_version_range", minecraftVersionRange)
        }

        props.forEach(inputs::property)
        filesMatching("**/lang/en_us.json") { // Defaults description to English translation
            expand(props)
            filteringCharset = "UTF-8"
        }

        filesMatching("fabric.mod.json") { expand(props) }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        if (mod.obfuscated) {
            val remapJar by existing(net.fabricmc.loom.task.RemapJarTask::class)
            val remapSourcesJar by existing(net.fabricmc.loom.task.RemapSourcesJarTask::class)
            from(remapJar, remapSourcesJar)
        } else {
            val sourcesJar by existing
            from(jar, sourcesJar)
        }

        into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
        dependsOn("build")
    }
}

val currentCommitHash: String by lazy {
    Runtime.getRuntime()
        .exec(arrayOf("git", "rev-parse", "--verify", "--short", "HEAD"), null, rootDir)
        .inputStream.bufferedReader().readText().trim()
}

blossom {
    replaceToken("@MODID@", mod.id)
    replaceToken("@VERSION@", mod.version)
    replaceToken("@DEVELOPMENT@", mod.development)
    replaceToken("@COMMIT@", currentCommitHash)
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(tasks.named("build"))
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)