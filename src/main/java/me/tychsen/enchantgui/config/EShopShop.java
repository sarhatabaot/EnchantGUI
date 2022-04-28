package me.tychsen.enchantgui.config;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.localization.LocalizationManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public class EShopShop extends AConfig {
    private static EShopShop instance;

    private EShopShop() {
        super("languages/"+EShopConfig.getLanguage()+"/shop.yml", Main.getInstance());
        saveDefaultConfiguration();
    }

    public void reload(@NotNull CommandSender sender) {
        reloadConfig();
        sender.sendMessage(LocalizationManager.getInstance().getString("prefix") + " " + LocalizationManager.getInstance().getString("shop-reloaded"));
    }

    @Override
    public String getString(String path, String def) {
        return getConfig().getString(path, def);
    }

    @Override
    public String getString(final String path) {
        return getConfig().getString(path);
    }

    public static EShopShop getInstance() {
        if (instance == null) {
            instance = new EShopShop();
        }
        return instance;
    }
}
