package me.tychsen.enchantgui.commands;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EshopConfig;
import me.tychsen.enchantgui.config.EshopShop;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.MenuSystem;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author sarhatabaot
 */
public class ShopCommand extends PlayerCommand {
    private MenuSystem menuSystem;

    public ShopCommand() {
        super("eshop");

        setAliases(Collections.singletonList("enchantgui"));
        setDescription("Command for the EnchantGUI plugin.");
        setUsage("/eshop");
        setPermission("eshop.use");
        menuSystem = Main.getMenuSystem();
    }

    @Override
    protected void run(final Player player, final String[] args) {
        if(args.length > 1){
            returnTell("&cToo many arguments.");
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("reload")){
            EshopConfig.getInstance().reloadConfig(player);
            LocalizationManager.getInstance().reload(player);
            EshopShop.getInstance().reload(player);
            return;
        }

        menuSystem.showMainMenu(player);

    }
}
