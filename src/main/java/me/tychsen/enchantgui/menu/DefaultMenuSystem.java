package me.tychsen.enchantgui.menu;

import me.tychsen.enchantgui.config.EshopConfig;
import me.tychsen.enchantgui.config.EshopShop;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.permissions.EshopPermissionSys;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DefaultMenuSystem implements MenuSystem {
    public static final String START =
            ChatColor.AQUA + LocalizationManager.getInstance().getString("prefix") + " " + ChatColor.WHITE;

    private Map<String, String[]> playerLevels;

    private EshopPermissionSys permsys;
    private EshopConfig config;
    private MenuGenerator generator;

    public DefaultMenuSystem() {
        playerLevels = new HashMap<>();
        permsys = new EshopPermissionSys();
        config = new EshopConfig();
        generator = new DefaultMenuGenerator(36, config, permsys);
    }

    @Override
    public void showMainMenu(Player p) {
        LocalizationManager lm = LocalizationManager.getInstance();
        if (playerLevels.containsKey(p.getName())) playerLevels.remove(p.getName());
        if (permsys.hasUsePermission(p)) {
            Inventory inv = generator.mainMenu(p);

            p.openInventory(inv);
        } else {
            p.sendMessage(ChatColor.DARK_RED + lm.getString("prefix") + " " +
                    ChatColor.RED + lm.getString("no-permission"));
        }
    }

    private boolean isGoBackItem(ItemStack item) {
        return item.getItemMeta().getDisplayName().equalsIgnoreCase("Go back")
                && item.getType() == Material.matchMaterial(EshopShop.getInstance().getString("shop.back-item"));
    }

    @Override
    public void handleMenuClick(Player p, InventoryClickEvent event) {
        if (playerLevels.containsKey(p.getName())) {
            if (isGoBackItem(event.getCurrentItem())) {
                playerLevels.remove(p.getName());
                showMainMenu(p);
            } else if (event.getCurrentItem().getType() != Material.AIR) {
                // TODO: Figure out better way of purchasing enchants.
                // For example a seperate class to call purchaseEnchant on.
                // Required to enable Enchanting Table menu opener..
                purchaseEnchant(p, event);
            }
        } else {
            // TODO: Better implementation of the "player levels".. enchantMenu should not modify playerLevels.
            Inventory inv = generator.enchantMenu(p, event.getCurrentItem(), playerLevels);
            p.openInventory(inv);
        }
    }

    private void purchaseEnchant(Player p, InventoryClickEvent event) {
        LocalizationManager lm = LocalizationManager.getInstance();
        ItemStack item = event.getCurrentItem();
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        ItemStack playerHand = p.getInventory().getItemInMainHand();

        int level = Integer.parseInt(playerLevels.get(p.getName())[event.getSlot()]);
        int price = config.getPrice(enchantment, level);

        Main.debug("Slot: " + event.getSlot() + " Level: " + level);

        if (playerHand.getType() == Material.AIR) {
            p.sendMessage(START + lm.getString("cant-enchant"));
            p.closeInventory();
            return;
        }

        if (enchantment.canEnchantItem(playerHand) || config.getIgnoreItemType()) {
            PaymentStrategy payment = config.getEconomy();

            if (payment.withdraw(p, price)) {
                enchantItem(playerHand, enchantment, level);
                String message = String.format("%s %s &d %s %d &f for &6 %d %s", START, lm.getString("item-enchanted"), item.getItemMeta().getDisplayName(), level, price, EshopConfig.getInstance().getEconomyCurrency());
                Common.tell(p,message);
                p.closeInventory();
            } else {
                p.sendMessage(START + lm.getString("insufficient-funds"));
            }
        } else {
            p.sendMessage(START + lm.getString("item-cant-be-enchanted"));
            //TODO p.closeInventory(); add option for this
        }
    }

    private void enchantItem(ItemStack playerHand, Enchantment enchantment, int level) {
        if (level > enchantment.getMaxLevel() || !enchantment.canEnchantItem(playerHand)) {
            // Unsafe enchant
            playerHand.addUnsafeEnchantment(enchantment, level);
        } else {
            // Safe, regular enchant
            playerHand.addEnchantment(enchantment, level);
        }
    }
}
