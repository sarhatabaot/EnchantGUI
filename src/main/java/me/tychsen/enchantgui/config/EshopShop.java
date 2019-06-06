package me.tychsen.enchantgui.config;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

/**
 * @author sarhatabaot
 */
public class EshopShop {
    private static final String FILE_NAME_SHOP = "shop.yml";
    private static EshopShop instance;
    private Plugin plugin;
    private File configFile;
    private FileConfiguration config;

    private EshopShop(){
        plugin = Main.getInstance();
        saveDefaultConfiguration();
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        String start = ChatColor.AQUA + LocalizationManager.getInstance().getString("prefix") + " " + ChatColor.WHITE;
        sender.sendMessage(start + LocalizationManager.getInstance().getString("shop-reloaded"));
    }

    public String getString(String string){
        return getConfig().getString(string);
    }

    private void saveDefaultConfiguration(){
        if(configFile == null){
            configFile = new File(plugin.getDataFolder(),FILE_NAME_SHOP);
        }
        if(!configFile.exists()){
            plugin.saveResource(FILE_NAME_SHOP,false);
        }
    }

    private void reloadConfig(){
        if(configFile == null){
            configFile = new File(plugin.getDataFolder(),FILE_NAME_SHOP);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        Reader defaultConfigStream;
        InputStream defaultConfigInputStream = plugin.getResource(FILE_NAME_SHOP);
        if (defaultConfigInputStream != null) {
            try {
                defaultConfigStream = new InputStreamReader(plugin.getResource(FILE_NAME_SHOP), "UTF8"); //TODO
            } catch (UnsupportedEncodingException e) {
                defaultConfigStream = new InputStreamReader(plugin.getResource(FILE_NAME_SHOP));
            }
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            config.setDefaults(defaultConfig);
        }
        Main.debug("Config reloaded.");
    }


    private FileConfiguration getConfig(){
        if(config==null)
            reloadConfig();
        return config;
    }

    public static EshopShop getInstance(){
        if(instance == null){
            instance = new EshopShop();
        }
        return instance;
    }
}
