package me.tychsen.enchantgui.localization;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LocalizationManager {
    private static final String FILE_NAME_LOCALIZATION = "localization.yml";
    private static LocalizationManager instance;
    private Plugin plugin;
    private File configFile;
    private FileConfiguration config;

    private LocalizationManager() {
        plugin = Main.getInstance();

        saveDefaultConfiguration();
    }

    public String getString(String key) {
        return Common.colorize(getConfig().getString(key));
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        String start = ChatColor.AQUA + LocalizationManager.getInstance().getString("prefix") + " " + ChatColor.WHITE;
        sender.sendMessage(start + LocalizationManager.getInstance().getString("localization-reloaded"));
    }

    private void saveDefaultConfiguration() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), FILE_NAME_LOCALIZATION);
        }
        if (!configFile.exists()) {
            plugin.saveResource(FILE_NAME_LOCALIZATION, false);
        }
    }

    private FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    private void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), FILE_NAME_LOCALIZATION);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        Reader defaultConfigStream;
        InputStream defaultConfigInputStream = plugin.getResource(FILE_NAME_LOCALIZATION);
        if (defaultConfigInputStream != null) {
            defaultConfigStream = new InputStreamReader(defaultConfigInputStream, StandardCharsets.UTF_8);
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            config.setDefaults(defaultConfig);
        }
    }

    /**
     * Get singleton instance of LocalizationManager.
     *
     * @return LocalizationManager instance as singleton.
     */
    public static LocalizationManager getInstance() {
        if (instance == null)
            instance = new LocalizationManager();

        return instance;
    }
}
