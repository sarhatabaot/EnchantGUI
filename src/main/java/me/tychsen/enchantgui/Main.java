package me.tychsen.enchantgui;

import me.tychsen.enchantgui.config.EshopConfig;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
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
        getLogger().info(getName()+" "+getDescription().getVersion() + "enabled!");
    }

    public static void debug(String msg){
        if(EshopConfig.getInstance().getDebug())
            Bukkit.getPluginManager().getPlugin("EnchantGUI").getLogger().warning(String.format("\u001B[33m DEBUG %s \u001B[0m",msg));
    }

    public static Main getInstance() {
        return instance;
    }
}