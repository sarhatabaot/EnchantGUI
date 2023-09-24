plugins {
    id("java-library")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("io.freefair.lombok") version "6.6.3"
    id("org.sonarqube") version "4.0.0.2929"
}

group = "me.tychsen"
version  = "1.7.5"

repositories {
    mavenCentral()
    maven (
        url = "https://repo.aikar.co/content/groups/aikar/" )
    maven (
        url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    )
    maven (
        url = "https://nexus.hc.to/content/repositories/pub_releases"
    )
    maven (
        url = "https://oss.sonatype.org/content/groups/public/"
    )
    maven (
        url = "https://repo.codemc.org/repository/maven-public"
    )
    maven (
        url = "https://jitpack.io"
    )
    maven (
        url = "https://repo.rosewooddev.io/repository/public/"
    )
    maven (
        url = "https://repo.mattstudios.me/artifactory/public/"
    )
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("org.black_ixx:playerpoints:3.2.6")
    
    implementation("dev.triumphteam:triumph-gui:3.1.4")
    library("net.kyori:adventure-api:4.13.0")
    
    implementation("com.github.sarhatabaot:krakencore:1.6.3")
    implementation("de.tr7zw:item-nbt-api:2.11.3")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    
    library("org.jetbrains:annotations:24.0.1")
}

bukkit {
    name = "EnchantGUI"
    version = project.version.toString()
    main = "me.tychsen.enchantgui.EnchantGUIPlugin"
    apiVersion = "1.17"
    website = "https://github.com/sarhatabaot/EnchantGUI"
    authors = listOf("Dentych", "sarhatabaot")
    softDepend = listOf("Vault", "PlayerPoints")
    
    permissions {
        register("eshop.use") {
            description = "Gives access to /eshop."
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
        register("eshop.admin") {
            description = "Gives access to /eshop reload."
        }
        register("eshop.all") {
            description = "Gives access to all enchants and all levels of the enchants."
        }
        register("eshop.enchanting-table") {
            description = "Gives access to the right click on enchanting table feature."
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    
    shadowJar {
        minimize()
        archiveFileName.set("EnchantGUI-${project.version}.jar")
        archiveClassifier.set("shadow")
        
        relocate ("org.bstats", "me.tychsen.enchantgui.util")
        relocate ("co.aikar.commands", "me.tychsen.enchantgui.acf")
        relocate ("co.aikar.locales", "me.tychsen.enchantgui.locales")
        relocate ("de.tr7zw", "me.tychsen.enchantgui.nbt")
        relocate ("com.github.sarhatabaot.kraken", "me.tychsen.enchantgui.kraken")
        relocate ("dev.triumphteam", "me.tychsen.enchantgui.gui")
    }
}


sonarqube {
    properties {
        property("sonar.projectKey", "sarhatabaot_EnchantGUI")
        property("sonar.organization", "sarhatabaot-github")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

