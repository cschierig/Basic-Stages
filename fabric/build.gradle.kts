import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val withApiJar = property("withApiJar").toString().toBoolean()
val withSourcesJar = property("withSourcesJar").toString().toBoolean()
val compatMods = property("compatMods").toString().toBoolean()
val modrinthId: String by project
val modrinthType: String by project
val withExampleMod = property("withExampleMod").toString().toBoolean()

val commonProject = project(":common")

architectury {
    platformSetupLoomIde()
    fabric()
}

val common by configurations.creating
val shadowBundle by configurations.creating
configurations {
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }

    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    getByName("developmentFabric").extendsFrom(common)

    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

dependencies {
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    common(project(":common", "namedElements")) { isTransitive = false }
    shadowBundle(project(":common", "transformProductionFabric"))

    modCompileOnly("dev.emi:emi-fabric:${libs.versions.emi.get()}:api")

    if (compatMods) {
        modImplementation(libs.compat.jade.fabric)
        modImplementation(libs.compat.sodium.fabric)
        modImplementation(libs.compat.modmenu.fabric)

        modLocalRuntime("dev.emi:emi-fabric:${libs.versions.emi.get()}")
    }
}

sourceSets {
    if (withExampleMod) {
        create("example") {
            compileClasspath += sourceSets.main.get().compileClasspath
            runtimeClasspath += sourceSets.main.get().runtimeClasspath

            resources {
                srcDir(file("src/example/generated"))
                exclude("src/example/generated/resources/.cache")
            }
        }
    }
    getByName("main") {
        resources {
            srcDir(file("src/main/generated"))
            exclude("src/main/generated/resources/.cache")
        }
    }
}

loom {
    val awPath = commonProject.file("src/main/resources/${modId}.accesswidener")
    if (awPath.exists()) {
        accessWidenerPath.set(awPath)
    }
    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run/client")
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run/server")
        }
        create("datagen") {
            inherit(getByName("client"))
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${commonProject.file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=immersive_crafting")

            runDir("build/datagen")
        }
        if (withExampleMod) {
            create("example") {
                client()
                configName = "Example Mod"
                ideConfigGenerated(true)
                source(project.sourceSets.getByName("example"))
                runDir("run/exampleClient")
            }
            create("exampleServer") {
                server()
                configName = "Example Mod Server"
                ideConfigGenerated(true)
                source(project.sourceSets.getByName("example"))
                runDir("run/exampleServer")
            }
            create("exampleDatagen") {
                inherit(getByName("example"))
                name("Example Mod Data Generation")
                vmArg("-Dfabric-api.datagen")
                vmArg("-Dfabric-api.datagen.output-dir=${file("src/example/generated")}")
                vmArg("-Dfabric-api.datagen.modid=ic_examples")

                runDir("build/exampleDatagen")
            }
        }
    }
}

tasks.getByName<ShadowJar>("shadowJar") {
    configurations = listOf(shadowBundle)
    archiveClassifier.set("dev-shadow")
}

tasks.withType<RemapJarTask>() {
    inputFile.set(tasks.getByName<ShadowJar>("shadowJar").archiveFile)
    dependsOn(tasks.getByName<ShadowJar>("shadowJar"))
}

tasks.withType<ProcessResources>() {
    from(commonProject.sourceSets.getByName("commonAssets").resources)
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    val files = ArrayList<String>()
    if (withSourcesJar) {
        files.add("sourcesJar")
    }
    if (withApiJar) {
        files.add("apiJar")
    }

    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set(modrinthId)
        versionNumber.set(project.version.toString())
        versionName.set(project.version.toString() + " - " + project.name.uppercaseFirstChar())
        versionType.set(modrinthType)
        uploadFile.set(tasks.named("remapJar"))
        additionalFiles.set(files.map { tasks.named(it) })
        syncBodyFrom.set(rootProject.file("README.md").readText())
        dependencies {
            required.project("fabric-api")
            optional.project("jade")
        }
        gameVersions.set(listOf(libs.versions.minecraft.get()))
        loaders.set(listOf("fabric", "quilt"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
    //tasks.named("modrinth") { dependsOn("runDatagen") }
}
