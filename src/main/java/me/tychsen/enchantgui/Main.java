package me.tychsen.enchantgui;

import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.commands.ShopCommand;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.util.Common;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Setter
    @Getter
    private static Main instance;
    @Getter
    @Setter
    private static Economy economy = null;
    @Getter
    @Setter
    private static MenuSystem menuSystem;


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
        Common.registerCommand(new ShopCommand());

        // Enable Metrics
        Metrics metrics = new Metrics(this);

        getLogger().info(getName() + " " + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        setInstance(null);
        setEconomy(null);
        setMenuSystem(null);
        getLogger().info(getName() + " " + getDescription().getVersion() + " disabled!");
    }

    public static String getMinecraftVersion(){
        return instance.getServer().getVersion();
    }

    public static void debug(String msg) {
        if (EShopConfig.getInstance().getDebug())
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