plugins {
    idea
    java
    `maven-publish`
    alias(libs.plugins.fabric.loom)
}

val modId: String by project

loom {
    val awFile = file("src/main/resources/${modId}.accesswidener")
    if (awFile.exists()) {
        accessWidenerPath.set(awFile)
    }

    mixin {
        defaultRefmapName.set("${modId}.refmap.json")
    }

    addRemapConfiguration("testModImplementation") {
        targetConfigurationName.set("test")
        onCompileClasspath = true
        onRuntimeClasspath = true
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })

    compileOnly(libs.mixin)

    modImplementation(libs.architectury.common)

    configurations.getByName("testModImplementation")(libs.fabric.loader)
    testImplementation(libs.fabric.loader.junit)
}

sourceSets {
    named("main") {
        resources {
            srcDir(file("src/main/generated"))
            exclude("src/main/generated/.cache")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun api(dep: ExternalModuleDependency) {
    dep.artifact {
        classifier = "api"
    }
}
