rootProject.name = "EnchantGUI"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
            plugin("lombok", "io.freefair.lombok").version("8.6")
            plugin("plugin-yml", "net.minecrell.plugin-yml.bukkit").version("0.6.0")
            plugin("sonarqube", "org.sonarqube").version("5.0.0.4638")

            library("spigot-api", "org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
            library("vault-api", "com.github.MilkBowl:VaultAPI:1.7.1")
            library("playerpoints-api", "org.black_ixx:playerpoints:3.2.7")
            library("triumph-gui", "dev.triumphteam:triumph-gui:3.1.10")
            library("adventure-api", "net.kyori:adventure-api:4.17.0")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.13.1")
            library("acf", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.0.2")
            library("annotations", "org.jetbrains:annotations:24.1.0")
            library("helper", "com.github.sarhatabaot:KrakenCore:1.6.3")
        }
    }
}