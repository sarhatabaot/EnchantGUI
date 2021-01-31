package me.tychsen.enchantgui.localization;

import me.tychsen.enchantgui.EnchantGUI;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LocalizationManager extends AConfig {
    private static LocalizationManager instance;

    public LocalizationManager(@NotNull final String fileName) {
        super(fileName, EnchantGUI.getInstance());
        saveDefaultConfiguration();
    }

    @Override
    public String getString(@NotNull String path) {
        return Common.colorize(getConfig().getString(path));
    }

    public void reload(@NotNull CommandSender sender) {
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
            instance = new LocalizationManager("localization_en.yml"); //should never get here..

        return instance;
    }
}
