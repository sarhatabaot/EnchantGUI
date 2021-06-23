package me.tychsen.enchantgui.localization;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.config.EShopConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class LocalizationManager extends AConfig {
    private static LocalizationManager instance;

    private LocalizationManager() {
        super("localization_" + EShopConfig.getLanguage() + ".yml", Main.getInstance());
        saveDefaultConfiguration();
    }

    @Override
    public String getString(String path) {
        return ChatUtil.colorize(getConfig().getString(path));
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        ChatUtil.tell(sender, instance.getString("prefix") + " " + instance.getString("localization-reloaded"));
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
