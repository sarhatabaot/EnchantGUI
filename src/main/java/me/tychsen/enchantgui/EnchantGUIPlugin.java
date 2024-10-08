package me.tychsen.enchantgui;

import co.aikar.commands.PaperCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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
    @Setter (AccessLevel.PRIVATE) @Getter
    private static EnchantGUIPlugin instance;
    
    @Getter @Setter
    private ShopMenu shopMenu;

    @Getter @Setter (AccessLevel.PRIVATE)
    private static Set<UUID> toggleRightClickPlayers = new HashSet<>();

    private EShopConfig config;
    @Getter
    private LocalizationManager lm;

    @Getter
    private PlayerPointsAPI ppApi;

    @Override
    public void onEnable() {
        setInstance(this);
        config = new EShopConfig();
        lm = new LocalizationManager();


        this.shopMenu = new ShopMenu(new Enchants());
        // Register event manager
        getServer().getPluginManager().registerEvents(new EventManager(),this);

        // Register command
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new ShopCommand());

        // Enable Metrics
        if(!getConfig().getBoolean("opt-out"))
            new Metrics(this, 3871);

        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " enabled!");
        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " using: "+ EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().name());
    }

    @Override
    public void onDisable() {
        setInstance(null);
        setToggleRightClickPlayers(null);
        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " disabled!");
    }

    public void onReload(){
        config.reloadConfig();

        hookPlayerPoints();
    }

    public static void debug(String msg) {
        if (EnchantGUIPlugin.getInstance().getMainConfig().getDebug())
            EnchantGUIPlugin.getInstance().getLogger().info(() -> String.format("DEBUG %s", msg));
    }


    private void hookPlayerPoints() {
        if(!config.getPaymentType().equalsIgnoreCase("playerpoints")) {
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


}