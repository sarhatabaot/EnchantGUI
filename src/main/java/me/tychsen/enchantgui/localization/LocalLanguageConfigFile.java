package me.tychsen.enchantgui.localization;


/**
 * @author sarhatabaot
 */
public class LocalLanguageConfigFile extends LocalizedConfigFile {
    public LocalLanguageConfigFile(final String lang) {
        super(lang, "localization.yml");
    }

    public String getPrefix() {
        return getConfig().getString("prefix","EnchantGUI");
    }

    public String getShopReloaded() {
        return getConfig().getString("shop-reloaded", "Shop file has been reloaded!");
    }
}
