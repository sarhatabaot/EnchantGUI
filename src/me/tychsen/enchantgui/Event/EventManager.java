package me.tychsen.enchantgui.Event;

import me.tychsen.enchantgui.Config.EshopConfig;
import me.tychsen.enchantgui.Menu.MenuSystem;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.Permissions.EshopPermissionSys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventManager implements Listener, CommandExecutor {
    private MenuSystem system;

    public EventManager(MenuSystem system) {
        this.system = system;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null ||  e.getCurrentItem().getType() == Material.AIR)
            return;
        try {
            handleInventoryClickEvent(e);
        } catch (Exception exc) {
            // TODO: Fix this shit with main getinstance.. it's ugly.
            Main.getInstance().getLogger().severe("EXCEPTION: " + exc.getMessage() + "\n" + exc.getStackTrace()[0].toString() + "\n\n");
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR &&
                e.getPlayer().getItemInHand().getType() == Material.ENCHANTMENT_TABLE) {
            handlePlayerInteractEvent(e);
        }
    }

    private void handleInventoryClickEvent(InventoryClickEvent e) {
        boolean correctEvent = e.getInventory().getName().toLowerCase().startsWith(EshopConfig.getInstance().getMenuName().toLowerCase());
        Player p;

        if (correctEvent && e.getWhoClicked() instanceof Player) {
            e.setCancelled(true); // Cancel whatever default action might occur
            p = (Player) e.getWhoClicked();

            system.handleMenuClick(p, e);
        } else {
            return;
        }

        /*
        switch (system.getPlayerMenuLevel(p)) {
            case 0:
                system.showEnchantPage(p, e.getCurrentItem());
                break;
            case 1:
                handleEnchantPage(p, e.getCurrentItem(), e.getSlot());
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
        */
    }

    private void handlePlayerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.isOp()) {
            // TODO: || playerHasUsePerms(p) <-- Implement this crap somewhere
            system.showMainMenu(e.getPlayer());
        }
    }

    private void handleEnchantPage(Player p, ItemStack item, int slot) {
        switch (slot) {
            case 27:
                system.showMainMenu(p);
                break;
            default:
                if (item.getType() != Material.AIR) {
                    // TODO: Fix
                    //system.purchaseEnchant(p, item, slot + 1);
                }
                break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("eshop") && sender instanceof Player) {
            handleCommand(sender, command, args);
        }

        return true;
    }

    private boolean handleCommand(CommandSender sender, Command cmd, String[] args) {
        Player p = (Player) sender;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                EshopConfig.getInstance().reloadConfig(sender);
            }
        } else {
            system.showMainMenu(p);
        }

        return true;
    }
}