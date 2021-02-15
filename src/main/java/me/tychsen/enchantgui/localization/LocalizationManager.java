package me.tychsen.enchantgui.localization;

import lombok.Setter;
import me.tychsen.enchantgui.EnchantGUI;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class LocalizationManager extends AConfig {
    @Setter
    private static LocalizationManager instance;

    public LocalizationManager(@NotNull final String fileName) {
        super(fileName, EnchantGUI.getInstance());
        EnchantGUI.debug(fileName);
        saveDefaultConfiguration();
    }

    @Override
    public String getString(@NotNull String path) {
        return Common.colorize(getConfig().getString(path));
    }

    public void reload(@NotNull CommandSender sender) {
        setFileName(Common.prependLanguage(EnchantGUI.getInstance().getConfig().getString("language")));
        EnchantGUI.debug(getFileName());
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
            instance = new LocalizationManager(Common.prependLanguage(EShopConfig.getLang())); //should never get here..

        return instance;
    }
}
