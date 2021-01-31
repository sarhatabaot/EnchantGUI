package me.tychsen.enchantgui;

import co.aikar.commands.BukkitCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.commands.ShopCommand;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.util.Common;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnchantGUI extends JavaPlugin implements Listener {
    @Setter (AccessLevel.PRIVATE)
    @Getter
    private static EnchantGUI instance;
    @Setter (AccessLevel.PRIVATE)
    @Getter
    private static Economy economy = null;
    @Getter
    @Setter
    private static MenuSystem menuSystem;
    @Getter
    @Setter (AccessLevel.PRIVATE)
    private static Set<UUID> toggleRightClickPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        setInstance(this);
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Generate config.yml if there is none
        saveDefaultConfig();
        saveDefaultConfigs();
        new LocalizationManager(
                Common.prependLanguage(
                        EShopConfig.getLang()));
        // Register event manager
        setMenuSystem(new DefaultMenuSystem());
        getServer().getPluginManager().registerEvents(new EventManager(getMenuSystem()), this);

        // Register command
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new ShopCommand());

        // Enable Metrics
        if(!getConfig().getBoolean("opt-out"))
            new Metrics(this, 3871);

        getLogger().info(getName() + " " + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        setInstance(null);
        setEconomy(null);
        setMenuSystem(null);
        setToggleRightClickPlayers(null);
        getLogger().info(getName() + " " + getDescription().getVersion() + " disabled!");
    }

    private void saveDefaultConfigs() {
        if (!new File("shop.yml").exists()) {
            saveResource("shop.yml", false);
        }

        if (!new File("localization_en.yml").exists()) {
            saveResource("localization_en.yml", false);
        }

        if (!new File("localization_he.yml").exists()) {
            saveResource("localization_he.yml", false);
        }

        if (!new File("localization_pt-br.yml").exists()) {
            saveResource("localization_pt-br.yml", false);
        }
    }

    public static String getMinecraftVersion(){
        return instance.getServer().getVersion();
    }

    public static void debug(@NotNull String msg) {
        if (EShopConfig.getDebug())
            EnchantGUI.getInstance().getLogger().warning(String.format("DEBUG %s", msg));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("could not find vault");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("could not find Economy.class");
            return false;
        }
        setEconomy(rsp.getProvider());
        return economy != null;
    }



}