package me.tychsen.enchantgui;

import lombok.Setter;
import me.tychsen.enchantgui.config.EshopConfig;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Setter
    private static Main instance;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        setInstance(this);
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Loading configs and stuff...");

        // Generate config.yml if there is none
        saveDefaultConfig();

        // Register event manager
        EventManager manager = new EventManager(new DefaultMenuSystem());
        getServer().getPluginManager().registerEvents(manager, this);

        // Register command executor
        getCommand("eshop").setExecutor(manager);

        // Enable Metrics
        Metrics metrics = new Metrics(this);
        getLogger().info(getName() + " " + getDescription().getVersion() + " enabled!");
    }

    public static void debug(String msg) {
        if (EshopConfig.getInstance().getDebug())
            Main.getInstance().getLogger().warning(String.format("\u001B[33m" + "DEBUG %s \u001B[0m", msg));
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
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Main getInstance() {
        return instance;
    }
}