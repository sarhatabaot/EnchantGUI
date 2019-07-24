package me.tychsen.enchantgui;

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
import me.tychsen.enchantgui.util.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static Main instance;
    @Setter(AccessLevel.PRIVATE)
    @Getter
    private static Economy economy = null;
    @Getter
    @Setter
    private static MenuSystem menuSystem;
    @Getter
    @Setter(AccessLevel.PRIVATE)
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
        Common.registerCommand(new ShopCommand());

        // Enable Metrics
        if (!getConfig().getBoolean("opt-out"))
            new Metrics(this);

        getLogger().info(getName() + " " + getDescription().getVersion() + " enabled!");

        UpdateChecker.init(this, 63972).requestUpdateCheck().whenComplete((result, e) -> {
                    if (result.requiresUpdate()) {
                        getLogger().info(String.format("New version available! Download EnchantGUI v%s from SpigotMC", result.getNewestVersion()));
                        return;
                    }
                    UpdateChecker.UpdateReason reason = result.getReason();

                    if(reason == UpdateChecker.UpdateReason.UP_TO_DATE){
                        getLogger().info(String.format("You are version of EnchantGUI (%s) is up to date.", result.getNewestVersion()));
                    } else if (reason == UpdateChecker.UpdateReason.UNRELEASED_VERSION){
                        getLogger().info(String.format("Your version of EnchantGUI (%s) is more recent than the one publicly available. Are you on a development build?", result.getNewestVersion()));
                    } else {
                        getLogger().warning("Could not check for a new version of EnchantGUI. Reason: "+reason);
                    }
                }
        );
    }

    @Override
    public void onDisable() {
        setInstance(null);
        setEconomy(null);
        setMenuSystem(null);
        setToggleRightClickPlayers(null);
        getLogger().info(getName() + " " + getDescription().getVersion() + " disabled!");
    }

    public static String getMinecraftVersion() {
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

    public static void tell(final CommandSender toWhom,final String... messages) {
        String prefix = LocalizationManager.getInstance().getString("prefix");
        for (final String message : messages)
            tell(toWhom, prefix+message);
    }




}