package me.tychsen.enchantgui.event;

import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.menu.MenuSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.tychsen.enchantgui.Main.debug;


public class EventManager implements Listener{
    private MenuSystem system;

    public EventManager(MenuSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onCreativeClickEvent(InventoryCreativeEvent event){
        onInventoryClickEvent(event);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        String inventoryName = e.getView().getTitle().toLowerCase();
        String configInventoryName = EShopConfig.getMenuName().toLowerCase();
        boolean correctEvent = inventoryName.startsWith(configInventoryName);

        if (correctEvent) {
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player)) return;
            if (e.getView().getBottomInventory().contains(e.getCurrentItem())) return;
            try {
                handleInventoryClickEvent(e);
            } catch (Exception ex) {
                Main.getInstance().getLogger().severe("Inventory click event couldn't be handled. "+ex.getMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (!EShopConfig.getBoolean("right-click-enchanting-table")) {
            return;
        }
        if (Main.getToggleRightClickPlayers().contains(e.getPlayer().getUniqueId()))
            return;
        if(!e.getPlayer().hasPermission("eshop.enchantingtable"))
            return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            e.setCancelled(true);
            handlePlayerInteractEvent(e);
        }
    }

    private void handleInventoryClickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;
        if (e.getInventory().getType() != InventoryType.CHEST) return;

        Player p = (Player) e.getWhoClicked();
        system.handleMenuClick(p, e);
    }

    private void handlePlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getPlayer().hasPermission("eshop.enchantingtable")) {
            if (Main.getToggleRightClickPlayers().contains(e.getPlayer().getUniqueId())) {
                return;
            }

            system.showMainMenu(e.getPlayer());
        }
    }

}
