val modId: String by project
val enabledPlatforms: String by project

architectury {
    common(enabledPlatforms.split(','))
}

dependencies {
    modImplementation(libs.fabric.loader)

    modImplementation(libs.compat.jade.fabric)
    modCompileOnly("dev.emi:emi-xplat-intermediary:${libs.versions.emi.get()}:api")

//     "testModImplementation"(libs.fabric.loader)
//     testImplementation(libs.fabric.loader.junit)
}

sourceSets {
    named("main") {
        resources {
            srcDir(file("src/main/generated"))
            exclude("src/main/generated/.cache")
        }
    }
    create("commonAssets") {
    }
}

tasks.withType<ProcessResources>() {
    from(sourceSets.getByName("commonAssets").resources)

}

loom {
    val awFile = file("src/main/resources/${modId}.accesswidener")
    if (awFile.exists()) {
        accessWidenerPath.set(awFile)
    }

    //   addRemapConfiguration("testModImplementation") {
    //       targetConfigurationName.set("test")
    //       onCompileClasspath = true
    //       onRuntimeClasspath = true
    //   }
}

// tasks.withType<Test> {
//     useJUnitPlatform()
// }
