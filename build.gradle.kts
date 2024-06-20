import net.fabricmc.loom.api.LoomGradleExtensionAPI
import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(libs.plugins.architectury.loom) apply false
    alias(libs.plugins.architectury.plugin)
    alias(libs.plugins.shadow) apply false
}

val modArchiveName: String by project
val modName: String by project
val modId: String by project
val modGroup: String by project
val author: String by project
val modVersion: String by project

architectury {
    minecraft = libs.versions.minecraft.get()
}

allprojects {
    val libs = rootProject.libs
    version = "${modVersion}+${libs.versions.minecraft.get()}"
    group = modGroup
}

subprojects {
    val libs = rootProject.libs
    apply(plugin = libs.plugins.architectury.loom.get().pluginId)
    apply(plugin = libs.plugins.architectury.plugin.get().pluginId)
    apply(plugin = "maven-publish")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    configure<BasePluginExtension> {
        archivesName.set("${modArchiveName}-${project.name}")
    }

    repositories {
        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
        }
        exclusiveContent {
            forRepository {
                maven("https://api.modrinth.com/maven") {
                    name = "Modrinth"
                }
            }
            filter {
                includeGroup("maven.modrinth")
            }
        }
        maven("https://maven.terraformersmc.com/") {
            name = "TerraformersMC"
        }
    }

    dependencies {
        "minecraft"(libs.minecraft)
        "mappings"(loom.layered {
            officialMojangMappings()
            // TODO: enable parchment for 1.21
            // parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
        })
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    tasks.withType<Jar>().configureEach {
        from("LICENSE") {
            rename { "${it}_${modName}" }
        }
    }

    tasks.named<Jar>("jar") {
        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to modName,
                    "Specification-Vendor" to author,
                    "Specification-Version" to archiveVersion,
                    "Implementation-Title" to modName,
                    "Implementation-Vendor" to author,
                    "Implementation-Version" to archiveVersion,
                    "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                    "Timestamp" to System.currentTimeMillis(),
                    "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                    "Built-On-Minecraft" to libs.versions.minecraft.get()
                )
            )
        }
    }

    tasks.withType<ProcessResources> {
        gradle.projectsEvaluated {
            from(project(":common").extensions.getByType(SourceSetContainer::class).getByName("commonAssets").resources)
        }
        filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
            expand(project.properties)
        }
    }
}

tasks.register("release") {
    dependsOn(project("fabric").tasks.named("modrinth").get())
    dependsOn(project("neoforge").tasks.named("modrinth").get())
    dependsOn(project("fabric").tasks.named("modrinthSyncBody").get())
}
