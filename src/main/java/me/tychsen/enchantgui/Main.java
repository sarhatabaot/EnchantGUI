package me.tychsen.enchantgui;

import co.aikar.commands.BukkitCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.commands.ShopCommand;
import me.tychsen.enchantgui.commands.ShopOldCommand;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    @Setter (AccessLevel.PRIVATE)
    @Getter
    private static Main instance;
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

        // Register event manager
        setMenuSystem(new DefaultMenuSystem());
        getServer().getPluginManager().registerEvents(new EventManager(getMenuSystem()), this);

        // Register command
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new ShopCommand(LocalizationManager.getInstance().getString("prefix")));
        //Common.registerCommand(new ShopOldCommand());

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

    public static String getMinecraftVersion(){
        return instance.getServer().getVersion();
    }

    public static void debug(String msg) {
        if (EShopConfig.getDebug())
            Main.getInstance().getLogger().warning(String.format("DEBUG %s", msg));
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