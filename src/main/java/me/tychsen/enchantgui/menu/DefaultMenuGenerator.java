package me.tychsen.enchantgui.menu;

import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopEnchants;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.economy.NullPayment;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import me.tychsen.enchantgui.ChatUtil;
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

//TODO: Cache itemstacks
public class DefaultMenuGenerator implements MenuGenerator {
    private final int inventorySize;
    private final EShopConfig config;
    private EShopEnchants enchants;
    private final EShopPermissionSys permSys;

    public DefaultMenuGenerator(int inventorySize, EShopConfig config, EShopPermissionSys permSys) {
        this.inventorySize = inventorySize;
        this.config = config;
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

    private List<ItemStack> showPerItem(List<ItemStack> itemList, @NotNull ItemStack item, Player p) {
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

    private ItemStack generateItemWithMeta(@NotNull ItemStack item, int level, Enchantment enchantment) {
        ItemStack temp = item.clone();
        ItemMeta meta = temp.getItemMeta();
        List<String> lores = new ArrayList<>();
        lores.add(format(level, "level"));

        if (!(config.getEconomy() instanceof NullPayment)) {
            lores.add(format(getPrice(enchantment, level), "price"));
        }

        meta.setLore(lores);
        temp.setItemMeta(meta);
        return temp;
    }

    private Inventory generateEnchantMenu(@NotNull Player p, @NotNull ItemStack item, Map<String, String[]> playerLevels) {
        Inventory inv = p.getServer().createInventory(p, inventorySize, getMenuName());
        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        List<ItemStack> itemList = new ArrayList<>();

        // Generate the correct items for the player.
        // Based on permissions or OP status.
        String[] enchantLevels = getEnchantLevels(enchantment);
        List<String> levels = new ArrayList<>();

        for (String enchantLevel : enchantLevels) {
            enchantLevel = enchantLevel.substring(5);
            int level = Integer.parseInt(enchantLevel);
            if (permSys.hasEnchantPermission(p, enchantment, level)) {
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

        playerLevels.put(p.getName(), levels.toArray(new String[0]));
        return inv;
    }

    private ItemStack generateBackItem() {
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
