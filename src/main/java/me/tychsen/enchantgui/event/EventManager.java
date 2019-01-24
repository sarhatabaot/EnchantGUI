package me.tychsen.enchantgui.event;

import me.tychsen.enchantgui.config.EshopConfig;
import me.tychsen.enchantgui.config.EshopShop;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.menu.MenuSystem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager implements Listener, CommandExecutor {
    private MenuSystem system;

    public EventManager(MenuSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        String inventoryName = e.getInventory().getName().toLowerCase();
        String configInventoryName = EshopConfig.getInstance().getMenuName().toLowerCase();
        boolean correctEvent = inventoryName.startsWith(configInventoryName);

        if (correctEvent) {
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player)) return;
            try {
                handleInventoryClickEvent(e);
            } catch (Exception ex) {
                Logger logger = Main.getInstance().getLogger();
                logger.log(Level.SEVERE, "Inventory click event couldn't be handled.", ex);
            }
        }
    }

    //@EventHandler
    // TODO: Implement Enchanting table right click to open menu.
    // Requires more work, though, as it's currently the item in the hand
    // that is enchanted - which will then be an enchanting table...
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR &&
                e.getPlayer().getInventory().getItemInMainHand().getType() == Material.ENCHANTING_TABLE) {
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
        Player p = e.getPlayer();
        if (p.isOp()) {
            // TODO: || playerHasUsePerms(p) <-- Implement this crap somewhere
            // IMPORTANT: PlayerInteractEvent EventHandler is currently disabled, so no worries...
            system.showMainMenu(e.getPlayer());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("eshop")) {
            handleCommand(sender, args);
        }

        return true;
    }

    private void handleCommand(CommandSender sender, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                EshopConfig.getInstance().reloadConfig(sender);
                LocalizationManager.getInstance().reload(sender);
                EshopShop.getInstance().reload(sender);
            }
        } else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                system.showMainMenu(p);
            } else {
                sender.sendMessage(LocalizationManager.getInstance().getString("command-from-console"));
            }
        }

    }
}
