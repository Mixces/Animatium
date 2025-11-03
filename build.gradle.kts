plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom)
    alias(libs.plugins.publishing)
    alias(libs.plugins.blossom)
    alias(libs.plugins.ksp)
    alias(libs.plugins.fletchingtable.fabric)
    alias(libs.plugins.fletchingtable.neoforge)
}

repositories {
    maven("https://maven.parchmentmc.org") // Parchment
    maven("https://maven.nucleoid.xyz/") // Placeholder API - required by Mod Menu
    maven("https://maven.neoforged.net/releases") // NeoForge
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://maven.bawnorton.com/releases") // MixinSquared
    maven("https://maven.terraformersmc.com/") // Mod Menu
    maven("https://maven.isxander.dev/releases") // YACL
}

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name")
    val version = property("mod.version")
    val group = property("mod.group").toString()
    val description = property("mod.description")
    val source = property("mod.source")
    val issues = property("mod.issues")
    val license = property("mod.license").toString()
    val modrinth = property("mod.modrinth")
    val curseforge = property("mod.curseforge")
    val discord = property("mod.discord")
}

class Dependencies {
    val neoforgeVersion = property("deps.neoforge_version") as String?
    val fabricLoaderVersion = property("deps.fabric_loader_version") as String?
    val fabricApiVersion = property("deps.fabric_api_version") as String?
    val devAuthVersion = property("deps.devauth_version") as String?
    val mixinconstraintsVersion = property("deps.mixinconstraints_version") as String?
    val mixinsquaredVersion = property("deps.mixinsquared_version") as String?
}

class LoaderData {
    val loader = loom.platform.get().name.lowercase()
    val isFabric = loader == "fabric"
    val isNeoforge = loader == "neoforge"
}

class McData {
    val version = property("mod.mc_version")
    val dep = property("mod.mc_dep").toString()
}

val mc = McData()
val mod = ModData()
val deps = Dependencies()
val loader = LoaderData()

version = "${mod.version}+${mc.version}-${loader.loader}"
group = mod.group
base { archivesName.set(mod.id) }

stonecutter {
    constants["fabric"] = loader.isFabric
    constants["neoforge"] = loader.isNeoforge
}

val currentCommitHash: String by lazy {
    Runtime.getRuntime()
        .exec("git rev-parse --verify --short HEAD", null, rootDir)
        .inputStream.bufferedReader().readText().trim()
}

blossom {
    replaceToken("@MODID@", mod.id)
    replaceToken("@VERSION@", mod.version)
    replaceToken("@COMMIT@", currentCommitHash) // if development version, put currentCommitHash else put ""
}

loom {
    silentMojangMappingsLicense()
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run"
    }

    runConfigs.remove(runConfigs["server"]) // Removes server run configs
    accessWidenerPath = rootProject.file("src/main/resources/animatium.accesswidener")
}

loom.runs {
    afterEvaluate {
        val mixinJarFile = configurations.runtimeClasspath.get().incoming.artifactView {
            componentFilter {
                it is ModuleComponentIdentifier && it.group == "net.fabricmc" && it.module == "sponge-mixin"
            }
        }.files.first()
        configureEach {
            vmArg("-javaagent:$mixinJarFile") // Mixin Hotswap doesn't work on NeoForge, but doesn't hurt to keep
            property("mixin.hotSwap", "true")
            property("mixin.debug.export", "true") // Puts mixin outputs in /run/.mixin.out
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

dependencies {
    minecraft("com.mojang:minecraft:${mc.version}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        // Mojmap mappings
        officialMojangMappings()
        // Parchment mappings (it adds parameter mappings & javadoc)
        optionalProp("deps.parchment_version") {
            parchment("org.parchmentmc.data:parchment-${mc.version}:$it@zip")
        }
    })

    modRuntimeOnly("me.djtheredstoner:DevAuth-${loader.loader}:${deps.devAuthVersion}")
    include(implementation("com.moulberry:mixinconstraints:${deps.mixinconstraintsVersion}")!!)!!
    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-${loader.loader}:${deps.mixinsquaredVersion}")!!)!!)
    if (loader.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:${deps.fabricLoaderVersion}")!!

        // Fabric API - Needs to be specified, otherwise an older version might be defaulted and cause issues.
        modImplementation(fabricApi.module("fabric-resource-loader-v0", deps.fabricApiVersion))
        modImplementation(fabricApi.module("fabric-networking-api-v1", deps.fabricApiVersion))
        modImplementation(fabricApi.module("fabric-command-api-v2", deps.fabricApiVersion))

        optionalProp("deps.modmenu_version") { prop ->
            modImplementation("com.terraformersmc:modmenu:$prop") {
                exclude(group, "net.fabricmc.fabric-api")
            }
        }

        optionalProp("deps.yacl_version") { prop ->
            modImplementation("dev.isxander:yet-another-config-lib:$prop") {
                exclude(group, "net.fabricmc.fabric-api")
            }
        }
    } else if (loader.isNeoforge) {
        "neoForge"("net.neoforged:neoforge:${deps.neoforgeVersion}")
    }
}

// mc_dep fields must be in the format 'x', '>=x', '>=x <=y'
val rangeRegex = Regex(""">=\s*([0-9.]+)(?:\s*<=\s*([0-9.]+))?""")
val exactVersionRegex = Regex("""^\d+\.\d+(\.\d+)?$""")

val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }
val curseforgeId = findProperty("publish.curseforge")?.toString()?.takeIf { it.isNotBlank() }

// accessTokens should be placed in the user Gradle gradle.properties file
// for example, on Windows this would be "C:\Users\{user}\.gradle\gradle.properties"
// then add:
// modrinth.token=
// curseforge.token=
publishMods {
    file = project.tasks.remapJar.get().archiveFile

    displayName = "${mod.name} ${mod.version}"
    this.version = mod.version.toString()
    changelog = project.rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = STABLE

    modLoaders.add(loader.loader)
    dryRun = modrinthId == null && curseforgeId == null
    if (modrinthId != null) {
        modrinth {
            projectId = property("publish.modrinth").toString()
            accessToken = findProperty("modrinth.token").toString()
            if (rangeRegex.matches(mc.dep)) {
                val match = rangeRegex.find(mc.dep)!!
                val minVersion = match.groupValues[1]
                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"
                minecraftVersionRange {
                    start = minVersion
                    end = maxVersion
                }
            } else if (exactVersionRegex.matches(mc.dep)) {
                minecraftVersions.add(mc.dep)
            }

            if (loader.isFabric) {
                requires("fabric-api")
                requires("yacl")
                optional("modmenu")
            }
        }
    }

    if (curseforgeId != null) {
        curseforge {
            projectId = property("publish.curseforge").toString()
            accessToken = findProperty("curseforge.token").toString()
            if (rangeRegex.matches(mc.dep)) {
                val match = rangeRegex.find(mc.dep)!!
                val minVersion = match.groupValues[1]
                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"
                minecraftVersionRange {
                    start = minVersion
                    end = maxVersion
                }
            } else if (exactVersionRegex.matches(mc.dep)) {
                minecraftVersions.add(mc.dep)
            }

            if (loader.isFabric) {
                requires("fabric-api")
                requires("yacl")
                optional("modmenu")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.processResources {
    val props = buildMap {
        put("id", mod.id)
        put("name", mod.name)
        put("version", mod.version)
        put("mcdep", mc.dep)
        put("description", mod.description)
        put("source", mod.source)
        put("issues", mod.issues)
        put("license", mod.license)
        put("modrinth", mod.modrinth)
        put("curseforge", mod.curseforge)
        put("discord", mod.discord)
        if (loader.isFabric) {
            put("fabric_loader_version", deps.fabricLoaderVersion)
        }

        if (loader.isNeoforge) {
            put("forge_version", deps.neoforgeVersion)
        }
    }

    props.forEach(inputs::property)
    filesMatching("**/lang/en_us.json") { // Defaults description to English translation
        expand(props)
        filteringCharset = "UTF-8"
    }

    if (loader.isFabric) {
        filesMatching("fabric.mod.json") { expand(props) }
        exclude(listOf("META-INF/neoforge.mods.toml"))
    }

    if (loader.isNeoforge) {
        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
        exclude("fabric.mod.json")
    }
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(tasks.named("build"))
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)