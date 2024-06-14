import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    idea
    java
    `maven-publish`
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.minotaur)
}

val modId: String by project
val withApiJar = property("withApiJar").toString().toBoolean()
val modrinthId: String by project
val modrinthType: String by project
val withExampleMod = property("withExampleMod").toString().toBoolean()

val common = project(":common")

val exampleModCompileOnlyApi by configurations.creating;
exampleModCompileOnlyApi.extendsFrom(configurations.modCompileOnlyApi.get())

sourceSets {
    if (withExampleMod) {
        create("example") {
            compileClasspath = sourceSets.main.get().compileClasspath
            compileClasspath += exampleModCompileOnlyApi
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
    val awPath = common.file("src/main/resources/${modId}.accesswidener")
    if (awPath.exists()) {
        accessWidenerPath.set(awPath)
    }
    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
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
        create("datagen") {
            inherit(getByName("client"))
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${common.file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=immersive_crafting")

            runDir("build/datagen")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    source(common.sourceSets.main.get().allSource)
}

tasks.withType<Javadoc>().configureEach {
    source(common.sourceSets.main.get().allJava)
}

tasks.named<Jar>("sourcesJar") {
    from(common.sourceSets.main.get().allSource)
}

tasks.withType<ProcessResources>() {
    from(common.sourceSets.main.get().resources)
}

if (withApiJar) {
    tasks.register<Jar>("apiJar") {
        archiveClassifier.set("api")
        dependsOn(tasks.named("remapJar"))
        from(zipTree(tasks.named("remapJar").get().outputs.files.asPath))
        include("fabric.mod.json")
        include("*.mixins.json")
        include("com/carlschierig/immersivecrafting/api/**")
    }

    tasks.named("build") {
        dependsOn(tasks.named("apiJar"))
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    modImplementation(libs.architectury.fabric)

    implementation(project(":common"))

    if (withApiJar && withExampleMod) {
        exampleModCompileOnlyApi(tasks.getByName("apiJar").outputs.files)
    }
}

if (System.getenv("MODRINTH_TOKEN") != null) {
    val files = ArrayList<String>()
    files.add("sourcesJar")
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
            // required.project("fabric-api")
        }
        gameVersions.set(listOf(libs.versions.minecraft.get()))
        loaders.set(listOf("fabric", "quilt"))
        detectLoaders.set(false)
        changelog.set(file("../CHANGELOG.md").readText())
    }
    tasks.named("modrinth") { dependsOn("runDatagen") }
}

fun api(dep: ExternalModuleDependency) {
    dep.artifact {
        classifier = "api"
    }
}
