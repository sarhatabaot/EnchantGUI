package me.tychsen.enchantgui.menu;

import me.tychsen.enchantgui.config.EShopEnchants;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface MenuGenerator {
    Inventory mainMenu(Player p);
    Inventory enchantMenu(Player p, ItemStack item, Map<String, String[]> playerLevels);
    EShopEnchants getShopEnchants();
    void setShopEnchants(EShopEnchants enchants);
}
