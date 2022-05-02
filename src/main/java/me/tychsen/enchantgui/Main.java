package me.tychsen.enchantgui;

import co.aikar.commands.PaperCommandManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.commands.ShopCommand;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.event.EventManager;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin {
    @Setter (AccessLevel.PRIVATE) @Getter
    private static Main instance;
    
    @Getter @Setter
    private static MenuSystem menuSystem;
    @Getter @Setter (AccessLevel.PRIVATE)
    private static Set<UUID> toggleRightClickPlayers = new HashSet<>();

    private EShopConfig config;
    private LocalizationManager lm;

    @Getter
    private PlayerPointsAPI ppApi;

    @Override
    public void onEnable() {
        setInstance(this);
        config = new EShopConfig();
        lm = new LocalizationManager();


        // Register event manager
        setMenuSystem(new DefaultMenuSystem());
        getServer().getPluginManager().registerEvents(new EventManager(getMenuSystem()), this);

        // Register command
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new ShopCommand());

        // Enable Metrics
        if(!getConfig().getBoolean("opt-out"))
            new Metrics(this, 3871);

        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " enabled!");
        getLogger().info(() -> getName() + " " + getDescription().getVersion() + " using: "+ Main.getInstance().getMainConfig().getPaymentStrategy().name());
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
        if (Main.getInstance().getMainConfig().getDebug())
            Main.getInstance().getLogger().warning(() -> String.format("DEBUG %s", msg));
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

    public LocalizationManager getLm() {
        return lm;
    }


}