import java.lang.Boolean.parseBoolean

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom)
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
    val development = parseBoolean(property("mod.development") as String)

    val minecraftVersion = property("mod.minecraft_version") as String
    val minecraftVersionRange = property("mod.minecraft_version_range") as String
}

class Dependencies {
    val fabricLoaderVersion = property("deps.fabric_loader_version") as String?
    val fabricApiVersion = property("deps.fabric_api_version") as String?
    val devAuthVersion = property("deps.devauth_version") as String?
    val lombokVersion = property("deps.lombok_version") as String?
    val mixinConstraintsVersion = property("deps.mixinconstraints_version") as String?
    val mixinSquaredVersion = property("deps.mixinsquared_version") as String?
}

class LoaderData {
    val name = loom.platform.get().name.lowercase()
    val isFabric = name == "fabric"
}

val mod = ModData()
val deps = Dependencies()
val loader = LoaderData()

version = "${mod.version}+${mod.minecraftVersion}-${loader.name}"
group = mod.group
base { archivesName.set(mod.id) }

stonecutter {
    constants["fabric"] = loader.isFabric
}

val currentCommitHash: String by lazy {
    Runtime.getRuntime()
        .exec("git rev-parse --verify --short HEAD", null, rootDir)
        .inputStream.bufferedReader().readText().trim()
}

blossom {
    replaceToken("@MODID@", mod.id)
    replaceToken("@VERSION@", mod.version)
    replaceToken(
        "@COMMIT@",
        if (mod.development) currentCommitHash else ""
    ) // if development version, put currentCommitHash else put ""
    replaceToken("@DEVELOPMENT@", mod.development)
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
            vmArg("-javaagent:$mixinJarFile")
            property("mixin.hotSwap", "true")
            property("mixin.debug.export", "true") // Puts mixin outputs in /run/.mixin.out
            property("devauth.enabled", "true")
            property("devauth.account", "main")
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
    minecraft("com.mojang:minecraft:${mod.minecraftVersion}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()

        // Parchment mappings (it adds parameter mappings & javadoc)
        optionalProp("deps.parchment_version") {
            parchment("org.parchmentmc.data:parchment-${mod.minecraftVersion}:$it@zip")
        }
    })

    compileOnly("org.projectlombok:lombok:${deps.lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${deps.lombokVersion}")
    modRuntimeOnly("me.djtheredstoner:DevAuth-${loader.name}:${deps.devAuthVersion}")

    include(implementation("com.moulberry:mixinconstraints:${deps.mixinConstraintsVersion}")!!)!!
    include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-${loader.name}:${deps.mixinSquaredVersion}")!!)!!)
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

    modLoaders.add(loader.name)
    dryRun = modrinthId == null && curseforgeId == null
    if (modrinthId != null) {
        modrinth {
            projectId = property("publish.modrinth").toString()
            accessToken = findProperty("modrinth.token").toString()
//            if (rangeRegex.matches(mc.dep)) {
//                val match = rangeRegex.find(mc.dep)!!
//                val minVersion = match.groupValues[1]
//                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"
//                minecraftVersionRange {
//                    start = minVersion
//                    end = maxVersion
//                }
//            } else if (exactVersionRegex.matches(mc.dep)) {
//                minecraftVersions.add(mc.dep)
//            }

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
//            if (rangeRegex.matches(mc.dep)) {
//                val match = rangeRegex.find(mc.dep)!!
//                val minVersion = match.groupValues[1]
//                val maxVersion = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() } ?: "latest"
//                minecraftVersionRange {
//                    start = minVersion
//                    end = maxVersion
//                }
//            } else if (exactVersionRegex.matches(mc.dep)) {
//                minecraftVersions.add(mc.dep)
//            }

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
        put("description", mod.description)
        put("source", mod.source)
        put("issues", mod.issues)
        put("license", mod.license)
        put("modrinth", mod.modrinth)
        put("curseforge", mod.curseforge)
        put("discord", mod.discord)
        put("minecraft_version_range", mod.minecraftVersionRange)
        if (loader.isFabric) {
            put("fabric_loader_version", deps.fabricLoaderVersion)
        }
    }

    props.forEach(inputs::property)
    filesMatching("**/lang/en_us.json") { // Defaults description to English translation
        expand(props)
        filteringCharset = "UTF-8"
    }

    if (loader.isFabric) {
        filesMatching("fabric.mod.json") { expand(props) }
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