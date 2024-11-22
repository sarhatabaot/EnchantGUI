plugins {
    id("java-library")
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.shadow)
    alias(libs.plugins.sonarqube)
}

group = "me.tychsen"
version  = "1.7.6"

repositories {
    mavenCentral()
    maven ("https://repo.aikar.co/content/groups/aikar/")
    maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven ("https://nexus.hc.to/content/repositories/pub_releases")
    maven ("https://oss.sonatype.org/content/groups/public/")
    maven ("https://repo.codemc.org/repository/maven-public")
    maven ("https://jitpack.io")
    maven ("https://repo.rosewooddev.io/repository/public/")
    maven ("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    compileOnly(libs.spigot.api)
    compileOnly(libs.vault.api)
    compileOnly(libs.playerpoints.api)
    
    implementation(libs.triumph.gui)
    library(libs.adventure.api)
    
    implementation(libs.helper)
    implementation(libs.nbt.api)
    implementation(libs.acf)
    implementation(libs.bstats)
    library(libs.boosted.yml)
    
    library(libs.annotations)
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
        relocate ("de.tr7zw.changeme.nbtapi", "me.tychsen.enchantgui.nbt")
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

