package me.tychsen.enchantgui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by denni on 26/01/2016.
 */
public interface MenuSystem {
    void showMainMenu(Player p);
    void handleMenuClick(Player p, InventoryClickEvent event);
    MenuGenerator getMenuGenerator();
}
