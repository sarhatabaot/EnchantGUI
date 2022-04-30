package me.tychsen.enchantgui.menu;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.NbtUtils;
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


public class DefaultMenuSystem implements MenuSystem {
    private final String prefix;

    private final Map<String, String[]> playerLevels;

    private final MenuGenerator generator;

    public DefaultMenuSystem() {
        playerLevels = new HashMap<>();
        generator = new DefaultMenuGenerator(36);
        prefix = Main.getInstance().getLm().getPrefix() + " ";
    }

    public void reload() {
        generator.reload();
    }

    private void tell(Player player, String message) {
        ChatUtil.sendMessage(player, prefix + message);
    }

    @Override
    public void showMainMenu(@NotNull Player player) {
        LocalizationManager lm = Main.getInstance().getLm();
        playerLevels.remove(player.getName());
        if (!EShopPermissionSys.hasUsePermission(player)) {
            tell(player, lm.getLanguageString("no-permission"));
            return;
        }

        player.openInventory(generator.mainMenu(player));
    }

    private boolean isGoBackItem(@NotNull ItemStack item) {
        return new NBTItem(item).getBoolean(NbtUtils.BACK_BUTTON);
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
        LocalizationManager lm = Main.getInstance().getLm();
        ItemStack item = event.getCurrentItem();
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        ItemStack playerHand = player.getInventory().getItemInMainHand();

        int level = Integer.parseInt(playerLevels.get(player.getName())[event.getSlot()]);
        int price = (int) Main.getInstance().getMainConfig().getPrice(enchantment, level);

        Main.debug("Slot: " + event.getSlot() + " Level: " + level);

        if (playerHand.getType() == Material.AIR) {
            tell(player, lm.getLanguageString("cant-enchant"));
            player.closeInventory();
            return;
        }

        if (enchantment.canEnchantItem(playerHand) || Main.getInstance().getMainConfig().getIgnoreItemType()) {
            PaymentStrategy payment = Main.getInstance().getMainConfig().getPaymentStrategy();

            if (payment.withdraw(player, price)) {
                enchantItem(playerHand, enchantment, level);
                String message = String.format("%s &d%s %d &ffor &6%d%s", lm.getLanguageString("item-enchanted"), item.getItemMeta().getDisplayName(), level, price, Main.getInstance().getMainConfig().getEconomyCurrency());
                tell(player, message);
                player.closeInventory();
            } else {
                tell(player, lm.getLanguageString("insufficient-funds"));
            }
        } else {
            tell(player, lm.getLanguageString("item-cant-be-enchanted"));
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
