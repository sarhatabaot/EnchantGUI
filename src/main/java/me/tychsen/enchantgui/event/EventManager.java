package me.tychsen.enchantgui.event;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;


public class EventManager implements Listener{
    private final MenuSystem system;

    public EventManager(MenuSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onCreativeClickEvent(InventoryCreativeEvent event){
        onInventoryClickEvent(event);
    }

    @EventHandler
    public void onInventoryClickEvent(@NotNull InventoryClickEvent e) {
        String inventoryName;
        try {
            inventoryName = e.getView().getTitle().toLowerCase();
        } catch (IllegalStateException exception){
            inventoryName = "";
        }
        //todo, this could cause problems if our inventory name is not unique
        String configInventoryName = Main.getInstance().getMainConfig().getMenuName().toLowerCase();
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
        if (!Main.getInstance().getMainConfig().getBoolean("right-click-enchanting-table")) {
            return;
        }
        if (Main.getToggleRightClickPlayers().contains(e.getPlayer().getUniqueId()))
            return;
        if(!e.getPlayer().hasPermission(EShopPermissionSys.ENCHANTING_TABLE))
            return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            e.setCancelled(true);
            handlePlayerInteractEvent(e);
        }
    }

    private void handleInventoryClickEvent(@NotNull InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getInventory().getType() != InventoryType.CHEST) return;

        Player p = (Player) event.getWhoClicked();
        system.handleMenuClick(p, event);
    }

    private void handlePlayerInteractEvent(@NotNull PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission(EShopPermissionSys.ENCHANTING_TABLE)) {
            if (Main.getToggleRightClickPlayers().contains(event.getPlayer().getUniqueId())) {
                return;
            }

            system.showMainMenu(event.getPlayer());
        }
    }

}
