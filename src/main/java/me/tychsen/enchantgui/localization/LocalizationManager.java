package me.tychsen.enchantgui.localization;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import me.tychsen.enchantgui.Main;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalizationManager {
    private final Map<String, Map<String, LocalizedConfigFile>> languages;

    public LocalizationManager() {
        languages = new HashMap<>();
        for(String lang: getSupportedLanguages()) {
            languages.putIfAbsent(lang,new HashMap<>());
            languages.get(lang).put("localization", new LocalLanguageConfigFile(lang));
            languages.get(lang).put("shop", new ShopLanguageConfigFile(lang));
        }
        final File languagesFolder = new File(Main.getInstance().getDataFolder(),"languages");
        if(!languagesFolder.exists()) {
            boolean made = languagesFolder.mkdirs();
            Main.getInstance().getLogger().info(() -> "Created folder: languages= %b".formatted(made));
        }

        for(Map.Entry<String, Map<String, LocalizedConfigFile>> entry: languages.entrySet()) {
            final File langFolder = new File(languagesFolder,entry.getKey());
            if(!langFolder.exists()) {
                boolean made = langFolder.mkdirs();
                Main.getInstance().getLogger().info(() -> "Created folder: %s= %b".formatted(entry.getKey(),made));
            }
            for(Map.Entry<String,LocalizedConfigFile> localizedConfigFileEntry: entry.getValue().entrySet()) {
                localizedConfigFileEntry.getValue().saveDefaultConfig();
            }

        }

    }

    public String getLanguageString(String path) {
        return ChatUtil.color(getActiveLanguageFile().getConfig().getString(path));
    }

    public void reload(CommandSender sender) {
        for(Map.Entry<String, Map<String,LocalizedConfigFile>> entry: languages.entrySet()){
            //reload the files (if you changed your local setup)
            entry.getValue().forEach((key, value) -> value.reloadConfig());
        }
        ChatUtil.sendMessage(sender, getPrefix() + " " + getActiveLanguageFile().getConfig().getString("localization-reloaded"));
    }



    public LocalLanguageConfigFile getActiveLanguageFile() {
        return (LocalLanguageConfigFile) languages.get(Main.getInstance().getMainConfig().getLanguage()).get("localization");
    }

    public ShopLanguageConfigFile getActiveShopFile() {
        return (ShopLanguageConfigFile) languages.get(Main.getInstance().getMainConfig().getLanguage()).get("shop");
    }

    @Contract(pure = true)
    //TODO We need to get this automatically... Perhaps a json file we can update using crowdin or something
    private @Unmodifiable List<String> getSupportedLanguages() {
        return List.of("en","he","pt-br");
    }

    public String getPrefix() {
        return ChatUtil.color(getActiveLanguageFile().getPrefix());
    }

    public String getShopReloaded() {
        return getActiveLanguageFile().getShopReloaded();
    }



}
