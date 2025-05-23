package me.tychsen.enchantgui;

import co.aikar.commands.PaperCommandManager;
import de.tr7zw.changeme.nbtapi.NBT;
import me.tychsen.enchantgui.commands.ShopCommand;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.Enchants;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.ShopMenu;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EnchantGUIPlugin extends JavaPlugin {
    private static EnchantGUIPlugin instance;
    private final Set<UUID> toggleRightClickPlayers = new HashSet<>();

    private ShopMenu shopMenu;
    private EShopConfig config;
    private LocalizationManager lm;

    private PlayerPointsAPI ppApi;

    @Override
    public void onEnable() {
        setInstance(this);
        config = new EShopConfig();
        lm = new LocalizationManager();

        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.shopMenu = new ShopMenu(new Enchants());
        // Register event manager
        getServer().getPluginManager().registerEvents(new EventManager(), this);

        // Register command
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new ShopCommand());

        // Enable Metrics
        if (!getConfig().getBoolean("opt-out")) {
            new Metrics(this, 3871);
        }

        if (isPaperServer()) {
            getLogger().warning(() -> "You can ignore the message above about Commands API, we are aware, and are waiting on a fix from the commands library.");
        }

        getLogger().info(() -> getNameWithVersion() + " enabled!");
        getLogger().info(() -> getNameWithVersion() + " using: " + EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().name());


    }

    private String getNameWithVersion() {
        return "%s %s".formatted(getName(), getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        setInstance(null);
        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " disabled!");
    }

    public void onReload() {
        config.reload();

        hookPlayerPoints();
    }

    public static void debug(String msg) {
        if (EnchantGUIPlugin.getInstance().getMainConfig().getDebug()) {
            EnchantGUIPlugin.getInstance().getLogger().info(() -> String.format("DEBUG %s", msg));
        }
    }


    private void hookPlayerPoints() {
        if (!config.getPaymentType().equalsIgnoreCase("playerpoints")) {
            return;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppApi = PlayerPoints.getInstance().getAPI();
        }
    }

    @NotNull
    public EShopConfig getMainConfig() {
        return config;
    }

    public static EnchantGUIPlugin getInstance() {
        return instance;
    }

    public static void setInstance(EnchantGUIPlugin instance) {
        EnchantGUIPlugin.instance = instance;
    }

    public LocalizationManager getLm() {
        return lm;
    }

    public Set<UUID> getToggleRightClickPlayers() {
        return toggleRightClickPlayers;
    }

    public PlayerPointsAPI getPpApi() {
        return ppApi;
    }

    public ShopMenu getShopMenu() {
        return shopMenu;
    }

    public boolean isPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true; // The class exists, meaning the server is Paper-based.
        } catch (ClassNotFoundException e) {
            return false; // The class doesn't exist, so it's not Paper.
        }
    }
}