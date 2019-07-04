package me.tychsen.enchantgui.commands;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopShop;
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
        setPrefix(LocalizationManager.getInstance().getString("prefix"));
        menuSystem = Main.getMenuSystem();
    }

    @Override
    protected void run(final Player player, final String[] args) {
        if(args.length > 1){
            returnTell("&cToo many arguments.");
        }

        if(args.length > 0 && args[0].equalsIgnoreCase("reload")){
            EShopConfig.getInstance().reloadConfig(player);
            LocalizationManager.getInstance().reload(player);
            EShopShop.getInstance().reload(player);
            return;
        }
        LocalizationManager lm = LocalizationManager.getInstance();
        if(args.length > 0 && args[0].equalsIgnoreCase("toggle")){
            if(!player.hasPermission("eshop.enchantingtable.toggle")) {
                tell(lm.getString("no-permission"));
                return;
            }

            if(Main.getToggleRightClickPlayers().contains(player.getUniqueId())){
                Main.getToggleRightClickPlayers().remove(player.getUniqueId());
                tell(lm.getString("toggle-on"));
            }
            else {
                Main.getToggleRightClickPlayers().add(player.getUniqueId());
                tell(lm.getString("toggle-off"));
            }
            return;
        }

        menuSystem.showMainMenu(player);

    }
}
