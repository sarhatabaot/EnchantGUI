package me.tychsen.enchantgui.menu;

import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EShopEnchants;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.economy.NullPayment;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.tychsen.enchantgui.config.EShopConfig.*;

public class DefaultMenuGenerator implements MenuGenerator {
    private final int inventorySize;
    private EShopEnchants enchants;
    private final EShopPermissionSys permSys;

    public DefaultMenuGenerator(int inventorySize, EShopPermissionSys permSys) {
        this.inventorySize = inventorySize;
        this.enchants = new EShopEnchants();
        this.permSys = permSys;
    }

    public Inventory mainMenu(@NotNull Player p) {
        Inventory inv = p.getServer().createInventory(p, inventorySize, getMenuName());
        generateMainMenu(p, inv);

        return inv;
    }

    public Inventory enchantMenu(Player p, ItemStack item, Map<String, String[]> playerLevels) {
        return generateEnchantMenu(p, item, playerLevels);
    }

    private @NotNull List<ItemStack> showPerItem(List<ItemStack> itemList, @NotNull ItemStack item, Player p) {
        List<ItemStack> modifiedList = new ArrayList<>(itemList);
        for (Enchantment enchantment : item.getEnchantments().keySet()) {
            if (enchantment.canEnchantItem(p.getInventory().getItemInMainHand())) {
                modifiedList.add(item);
            }
        }
        return modifiedList;
    }

    private void generateMainMenu(Player p, Inventory inv) {
        List<ItemStack> enchantList = enchants.getEnchantList();
        List<ItemStack> itemList = new ArrayList<>();

        for (ItemStack item : enchantList) {
            if (permSys.hasEnchantPermission(p, item)) {
                if (getShowPerItem()) {
                    itemList = showPerItem(itemList, item, p);
                } else {
                    itemList.add(item);
                }
            }
        }
        inv.setContents(itemList.toArray(new ItemStack[0]));
    }

    @NotNull
    private String format(int type, String name) {
        String string = EShopShop.getInstance().getString("shop." + name);
        return MessageFormat.format(ChatUtil.colorize(string), type);
    }

    private @NotNull ItemStack generateItemWithMeta(@NotNull ItemStack item, int level, Enchantment enchantment) {
        ItemStack temp = item.clone();
        ItemMeta meta = temp.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(format(level, "level"));

        if (!(getPaymentStrategy() instanceof NullPayment)) {
            lore.add(format(getPrice(enchantment, level), "price"));
        }

        meta.setLore(lore);
        temp.setItemMeta(meta);
        return temp;
    }

    private @NotNull Inventory generateEnchantMenu(@NotNull Player player, @NotNull ItemStack item, Map<String, String[]> playerLevels) {
        Inventory inv = player.getServer().createInventory(player, inventorySize, getMenuName());
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        List<ItemStack> itemList = new ArrayList<>();

        // Generate the correct items for the player.
        // Based on permissions or OP status.
        String[] enchantLevels = getEnchantLevels(enchantment);
        List<String> levels = new ArrayList<>();

        for (String enchantLevel : enchantLevels) {
            enchantLevel = enchantLevel.substring(5);
            int level = Integer.parseInt(enchantLevel);
            if (permSys.hasEnchantPermission(player, enchantment, level)) {
                //TODO: Upgrade option, pass the original item as an object and compare the enchantments. Make sure to account for negative price.

                itemList.add(generateItemWithMeta(item, level, enchantment));
                Main.debug(item.toString());
                levels.add(enchantLevel);
            }
        }

        // Put the generated item list in the inventory
        inv.setContents(itemList.toArray(new ItemStack[0]));

        // Generate and insert a back button
        inv.setItem(27, generateBackItem());

        playerLevels.put(player.getName(), levels.toArray(new String[0]));
        return inv;
    }

    private @NotNull ItemStack generateBackItem() {
        Material material = Material.matchMaterial(EShopShop.getInstance().getString("shop.back-item"));

        if(material == null)
            material = Material.EMERALD;

        ItemStack backItem = new ItemStack(material);
        ItemMeta meta = backItem.getItemMeta();
        meta.setDisplayName("Go back");
        backItem.setItemMeta(meta);
        return backItem;
    }

    @Override
    public EShopEnchants getShopEnchants() {
        return enchants;
    }

    @Override
    public void setShopEnchants(final EShopEnchants enchants) {
        this.enchants = enchants;
    }
}
