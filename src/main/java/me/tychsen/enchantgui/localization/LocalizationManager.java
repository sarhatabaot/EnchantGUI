package me.tychsen.enchantgui.localization;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LocalizationManager extends AConfig {
    private static LocalizationManager instance;

    private LocalizationManager() {
        super("localization.yml", Main.getInstance());
        saveDefaultConfiguration();
    }

    public String getString(String key) {
        return Common.colorize(getConfig().getString(key));
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        Common.tell(sender,instance.getString("prefix") +" "+ instance.getString("localization-reloaded"));
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
