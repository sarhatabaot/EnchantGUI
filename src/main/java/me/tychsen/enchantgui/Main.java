package me.tychsen.enchantgui;

import me.tychsen.enchantgui.Config.EshopConfig;
import me.tychsen.enchantgui.Event.EventManager;
import me.tychsen.enchantgui.Menu.DefaultMenuSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

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
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().severe("Couldn't start Metrics.");
        }
    }

    public static void debug(String msg){
        if(EshopConfig.getInstance().getDebug())
            Bukkit.getPluginManager().getPlugin("EnchantGUI").getLogger().warning("\u001B[33m"+"[DEBUG] "+msg+"\u001B[0m");
    }

    public static Main getInstance() {
        return instance;
    }
}