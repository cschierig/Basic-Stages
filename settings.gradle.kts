pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.neoforged.net/releases") {
            name = "Neoforge"
        }
        maven("https://files.minecraftforge.net/maven/") {
            name = "Architectury"
        }
    }
}

val modArchiveName: String by extra
rootProject.name = modArchiveName
include("common", "fabric", "neoforge")
