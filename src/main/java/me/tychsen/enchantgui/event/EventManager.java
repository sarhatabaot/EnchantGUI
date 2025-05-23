package me.tychsen.enchantgui.event;

import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;


public class EventManager implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (EnchantGUIPlugin.getInstance().getMainConfig().isNotRightClickEnchantingTable()) {
            return;
        }
        if (EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().contains(e.getPlayer().getUniqueId())) {
            return;
        }
        if (!e.getPlayer().hasPermission(EShopPermissionSys.ENCHANTING_TABLE)) {
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
                e.getClickedBlock() != null &&
                e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            e.setCancelled(true);
            handlePlayerInteractEvent(e);
        }
    }


    private void handlePlayerInteractEvent(@NotNull PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission(EShopPermissionSys.ENCHANTING_TABLE)) {
            if (EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().contains(event.getPlayer().getUniqueId())) {
                return;
            }

            EnchantGUIPlugin.getInstance().getShopMenu().showMainMenu(event.getPlayer());
        }
    }

}
