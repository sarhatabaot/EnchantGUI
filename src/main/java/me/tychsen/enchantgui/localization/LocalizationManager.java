package me.tychsen.enchantgui.localization;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import com.github.sarhatabaot.kraken.core.file.FileUtil;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalizationManager {
    private List<String> supportedLanguages;
    private final Map<String, Map<String, LocalizedConfigFile>> languages;

    public LocalizationManager() {
        languages = new HashMap<>();
        for (String lang : getSupportedLanguages()) {
            languages.putIfAbsent(lang, new HashMap<>());
            languages.get(lang).put("localization", new LocalLanguageConfigFile(lang));
            languages.get(lang).put("shop", new ShopLanguageConfigFile(lang));
        }
        final File languagesFolder = new File(EnchantGUIPlugin.getInstance().getDataFolder(), "languages");
        if (!languagesFolder.exists()) {
            boolean made = languagesFolder.mkdirs();
            EnchantGUIPlugin.getInstance().getLogger().info(() -> "Created folder: languages= %b".formatted(made));
        }

        for (Map.Entry<String, Map<String, LocalizedConfigFile>> entry : languages.entrySet()) {
            final File langFolder = new File(languagesFolder, entry.getKey());
            if (!langFolder.exists()) {
                boolean made = langFolder.mkdirs();
                EnchantGUIPlugin.getInstance().getLogger().info(() -> "Created folder: %s= %b".formatted(entry.getKey(), made));
            }
            for (Map.Entry<String, LocalizedConfigFile> localizedConfigFileEntry : entry.getValue().entrySet()) {
                localizedConfigFileEntry.getValue().saveDefaultConfig();
            }

        }

    }

    public String getLanguageString(String path) {
        return ChatUtil.color(getActiveLanguageFile().getConfig().getString(path));
    }

    public void reload(CommandSender sender) {
        for (Map.Entry<String, Map<String, LocalizedConfigFile>> entry : languages.entrySet()) {
            //reload the files (if you changed your local setup)
            entry.getValue().forEach((key, value) -> value.reloadConfig());
        }
        ChatUtil.sendMessage(sender, getPrefix() + " " + getActiveLanguageFile().getConfig().getString("localization-reloaded"));
    }


    public LocalLanguageConfigFile getActiveLanguageFile() {
        return (LocalLanguageConfigFile) languages.get(EnchantGUIPlugin.getInstance().getMainConfig().getLanguage()).get("localization");
    }

    public ShopLanguageConfigFile getActiveShopFile() {
        return (ShopLanguageConfigFile) languages.get(EnchantGUIPlugin.getInstance().getMainConfig().getLanguage()).get("shop");
    }

    @Contract(pure = true)
    private @Unmodifiable List<String> getSupportedLanguages() {
        if(supportedLanguages == null)
            this.supportedLanguages = FileUtil.getFileNamesInJar(EnchantGUIPlugin.getInstance(), p -> p.getName().startsWith("languages") && p.isDirectory());
        return supportedLanguages;
    }

    public String getPrefix() {
        return ChatUtil.color(getActiveLanguageFile().getPrefix());
    }

    public String getShopReloaded() {
        return ChatUtil.color(getActiveLanguageFile().getShopReloaded());
    }


}
