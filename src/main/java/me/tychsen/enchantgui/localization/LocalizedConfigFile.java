package me.tychsen.enchantgui.localization;

import com.github.sarhatabaot.kraken.core.config.ConfigFile;
import me.tychsen.enchantgui.EnchantGUIPlugin;

import java.io.File;

/**
 * @author sarhatabaot
 */
public class LocalizedConfigFile extends ConfigFile<EnchantGUIPlugin> {
    public LocalizedConfigFile(final String lang, final String fileName) {
        super(EnchantGUIPlugin.getInstance(), "languages" + File.separator + lang + File.separator, fileName, "languages" + File.separator + lang);
    }

    public String getString(final String path) {
        return getConfig().getString(path);
    }

    public String getString(final String path, final String def) {
        return getConfig().getString(path, def);
    }
}
