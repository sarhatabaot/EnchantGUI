package me.tychsen.enchantgui.config;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.localization.LocalizationManager;
import org.bukkit.command.CommandSender;

/**
 * @author sarhatabaot
 */
public class EShopShop extends AConfig{
    private static EShopShop instance;

    private EShopShop(){
        super("shop.yml", Main.getInstance());
        saveDefaultConfiguration();
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        sender.sendMessage(LocalizationManager.getInstance().getString("prefix") +" "+ LocalizationManager.getInstance().getString("shop-reloaded"));
    }

    @Override
    public String getString(String path){
        return getConfig().getString(path);
    }

    public static EShopShop getInstance(){
        if(instance == null){
            instance = new EShopShop();
        }
        return instance;
    }
}
