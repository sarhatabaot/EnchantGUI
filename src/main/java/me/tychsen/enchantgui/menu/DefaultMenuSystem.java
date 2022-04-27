package me.tychsen.enchantgui.menu;

import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static me.tychsen.enchantgui.config.EShopConfig.getIgnoreItemType;
import static me.tychsen.enchantgui.config.EShopConfig.getPrice;

public class DefaultMenuSystem implements MenuSystem {
    public static String PREFIX;

    private final Map<String, String[]> playerLevels;

    private final EShopPermissionSys permsys;

    private final MenuGenerator generator;

    public DefaultMenuSystem() {
        playerLevels = new HashMap<>();
        permsys = new EShopPermissionSys();
        new EShopConfig();
        generator = new DefaultMenuGenerator(36, permsys);
        DefaultMenuSystem.PREFIX = LocalizationManager.getInstance().getString("prefix") + " ";
    }

    private void tell(Player player, String message) {
        ChatUtil.tell(player, PREFIX + message);
    }

    @Override
    public void showMainMenu(@NotNull Player player) {
        LocalizationManager lm = LocalizationManager.getInstance();
        playerLevels.remove(player.getName());
        if (!permsys.hasUsePermission(player)) {
            tell(player, lm.getString("no-permission"));
            return;
        }

        player.openInventory(generator.mainMenu(player));
    }

    private boolean isGoBackItem(@NotNull ItemStack item) {
        return item.getItemMeta().getDisplayName().equalsIgnoreCase("Go back")
                && item.getType() == Material.matchMaterial(EShopShop.getInstance().getString("shop.back-item"));
    }

    @Override
    public void handleMenuClick(@NotNull Player p, InventoryClickEvent event) {
        if (playerLevels.containsKey(p.getName())) {
            if (isGoBackItem(event.getCurrentItem())) {
                playerLevels.remove(p.getName());
                showMainMenu(p);
            } else if (event.getCurrentItem().getType() != Material.AIR) {
                purchaseEnchant(p, event);
            }
        } else {
            Inventory inv = generator.enchantMenu(p, event.getCurrentItem(), playerLevels);
            p.openInventory(inv);
        }
    }

    @Override
    public MenuGenerator getMenuGenerator() {
        return generator;
    }

    private void purchaseEnchant(@NotNull Player player, @NotNull InventoryClickEvent event) {
        LocalizationManager lm = LocalizationManager.getInstance();
        ItemStack item = event.getCurrentItem();
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        ItemStack playerHand = player.getInventory().getItemInMainHand();

        int level = Integer.parseInt(playerLevels.get(player.getName())[event.getSlot()]);
        int price = getPrice(enchantment, level);

        Main.debug("Slot: " + event.getSlot() + " Level: " + level);

        if (playerHand.getType() == Material.AIR) {
            tell(player, lm.getString("cant-enchant"));
            player.closeInventory();
            return;
        }

        if (enchantment.canEnchantItem(playerHand) || getIgnoreItemType()) {
            PaymentStrategy payment = EShopConfig.getPaymentStrategy();

            if (payment.withdraw(player, price)) {
                enchantItem(playerHand, enchantment, level);
                String message = String.format("%s &d%s %d &ffor &6%d%s", lm.getString("item-enchanted"), item.getItemMeta().getDisplayName(), level, price, EShopConfig.getEconomyCurrency());
                tell(player, message);
                player.closeInventory();
            } else {
                tell(player, lm.getString("insufficient-funds"));
            }
        } else {
            tell(player, lm.getString("item-cant-be-enchanted"));
        }
    }

    private void enchantItem(ItemStack playerHand, @NotNull Enchantment enchantment, int level) {
        if (level > enchantment.getMaxLevel() || !enchantment.canEnchantItem(playerHand)) {
            // Unsafe enchant
            playerHand.addUnsafeEnchantment(enchantment, level);
        } else {
            // Safe, regular enchant
            playerHand.addEnchantment(enchantment, level);
        }
    }
}
