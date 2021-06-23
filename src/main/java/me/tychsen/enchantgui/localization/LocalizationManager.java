package me.tychsen.enchantgui.localization;


import de.themoep.minedown.MineDown;
import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.config.EShopConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.AConfig;
import me.tychsen.enchantgui.config.EShopConfig;
import org.bukkit.command.CommandSender;

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

    public BaseComponent[] parseString(String path) {
        return MineDown.parse(getConfig().getString(path) );
    }

    public void reload(CommandSender sender) {
        Main.debug(EShopConfig.getLanguage());
        setFileName("localization_" + EShopConfig.getLanguage() + ".yml");
        saveDefaultConfiguration();
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
