package me.tychsen.enchantgui.menu;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.NbtUtils;
import me.tychsen.enchantgui.config.Enchants;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class ShopMenu {
    private final Enchants enchants;

    public ShopMenu(final Enchants enchants) {
        this.enchants = enchants;
    }

    public void showMainMenu(Player player) {
        Gui gui = Gui.gui(GuiType.CHEST)
                .rows(4)
                .title(Component.text(EnchantGUIPlugin.getInstance().getMainConfig().getMenuName()))
                .create();

        List<ItemStack> enchantList = enchants.getEnchantList();
        for (ItemStack itemStack : enchantList) {
            GuiItem guiItem = new GuiItem(itemStack);
            guiItem.setAction(event -> generateEnchantMenu(gui, itemStack, player).open(player));

            if (!EnchantGUIPlugin.getInstance().getMainConfig().getShowPerItem()) {
                gui.addItem(guiItem);
            }

            if (isShowPerItem(itemStack, player.getInventory().getItemInMainHand()))
                gui.addItem(guiItem);

            EnchantGUIPlugin.debug("ShowPerItem= %b".formatted(EnchantGUIPlugin.getInstance().getMainConfig().getShowPerItem()));
            EnchantGUIPlugin.debug("IsShowPerItem= %b".formatted(isShowPerItem(itemStack, player.getInventory().getItemInMainHand())));
            EnchantGUIPlugin.debug("ShopItem= %s".formatted(itemStack.toString()));
            EnchantGUIPlugin.debug("HeldItem= %s".formatted(player.getInventory().getItemInMainHand().toString()));
        }

        gui.open(player);
    }

    private boolean isShowPerItem(final @NotNull ItemStack shopEnchant, final ItemStack itemInMainHand) {
        for (Enchantment enchantment : shopEnchant.getEnchantments().keySet()) {
            if (enchantment.canEnchantItem(itemInMainHand)) {
                return true;
            }
        }
        return false;
    }

    private @NotNull Gui generateEnchantMenu(final Gui sourceGui, final @NotNull ItemStack item, final Player player) {
        Gui gui = Gui.gui(GuiType.CHEST)
                .rows(4)
                .title(Component.text(EnchantGUIPlugin.getInstance().getMainConfig().getMenuName()))
                .create();

        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        for (final ItemStack itemStack : generatePurchaseMenuItems(player, item, enchantment)) {
            GuiItem guiItem = new GuiItem(itemStack);
            guiItem.setAction(event -> {
                purchaseEnchant(player, event.getCurrentItem());
                gui.close(player);
            });
            gui.addItem(guiItem);
        }

        final String material = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.back-item.material", "EMERALD");
        final String displayName = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.back-item.display-name", "Go back");
        final int slot = (gui.getRows() * 9) - 9;
        gui.setItem(slot, ItemBuilder
                .from(Material.matchMaterial(material))
                .name(Component.text(displayName))
                .asGuiItem(event -> sourceGui.open(player)));
        return gui;
    }

    private void purchaseEnchant(@NotNull Player player, final ItemStack item) {
        if (item == null)
            return;
        LocalizationManager lm = EnchantGUIPlugin.getInstance().getLm();
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        ItemStack playerHand = player.getInventory().getItemInMainHand();
        int level = new NBTItem(item).getInteger(NbtUtils.LEVEL);
        double price = new NBTItem(item).getDouble(NbtUtils.PRICE);


        if (playerHand.getType() == Material.AIR) {
            tell(player, lm.getLanguageString("cant-enchant"));
            return;
        }
        if (!enchantment.canEnchantItem(playerHand) && !EnchantGUIPlugin.getInstance().getMainConfig().getIgnoreItemType()) {
            tell(player, lm.getLanguageString("item-cant-be-enchanted"));
            return;
        }


        PaymentStrategy payment = EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy();
        if (!payment.hasSufficientFunds(player, price)) {
            tell(player, lm.getLanguageString("insufficient-funds"));
            return;
        }
        
        payment.withdraw(player,price);
        enchantItem(playerHand, enchantment, level);
        tell(player, "%s &d%s %d &ffor &6%.2f%s".formatted(lm.getLanguageString("item-enchanted"), item.getItemMeta().getDisplayName(), level, price, EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().getCurrency()));
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

    public void reload() {
        this.enchants.reload();
    }

    private @NotNull List<ItemStack> generatePurchaseMenuItems(final Player player, final ItemStack item, final Enchantment enchantment) {
        List<ItemStack> itemList = new ArrayList<>();
        String[] enchantLevels = EnchantGUIPlugin.getInstance().getMainConfig().getEnchantLevels(enchantment);

        if (item == null)
            return Collections.emptyList();

        for (String enchantLevel : enchantLevels) {
            enchantLevel = enchantLevel.substring(5);
            int level = Integer.parseInt(enchantLevel);
            if (!EShopPermissionSys.hasEnchantPermission(player, enchantment, level)) {
                continue;
            }

            itemList.add(generateItemWithMeta(item, level, enchantment));
            //TODO: Upgrade option, pass the original item as an object and compare the enchantments. Make sure to account for negative price.
            EnchantGUIPlugin.debug(item.toString());
        }
        return itemList;
    }

    private @NotNull ItemStack generateItemWithMeta(@NotNull ItemStack item, int level, Enchantment enchantment) {
        ItemStack tempItem = item.clone();
        ItemMeta meta = tempItem.getItemMeta();

        double price = EnchantGUIPlugin.getInstance().getMainConfig().getPrice(enchantment, level);

        List<String> lore = new ArrayList<>();
        lore.add(formatLevel(level));
        lore.add(formatPrice(price));

        meta.setLore(lore);
        tempItem.setItemMeta(meta);
        tempItem.setAmount(level);
        NBTItem nbtItem = new NBTItem(tempItem);
        nbtItem.setInteger(NbtUtils.LEVEL, level);
        nbtItem.setDouble(NbtUtils.PRICE, price);

        return nbtItem.getItem();
    }

    @NotNull
    private String formatLevel(int type) {
        String string = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.level");
        return MessageFormat.format(ChatUtil.color(string), type);
    }

    @NotNull
    private String formatPrice(double type) {
        String string = EnchantGUIPlugin.getInstance().getLm().getActiveShopFile().getString("shop.price");
        return MessageFormat.format(ChatUtil.color(string), type);
    }

    private void tell(Player player, String message) {
        ChatUtil.sendMessage(player, EnchantGUIPlugin.getInstance().getLm().getPrefix() + message);
    }
}
