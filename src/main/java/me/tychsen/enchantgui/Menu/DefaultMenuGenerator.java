package me.tychsen.enchantgui.Menu;

import me.tychsen.enchantgui.Config.EshopConfig;
import me.tychsen.enchantgui.Config.EshopEnchants;
import me.tychsen.enchantgui.Economy.NullPayment;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.Permissions.EshopPermissionSys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultMenuGenerator implements MenuGenerator {
    private int inventorySize;
    private EshopConfig config;
    private EshopEnchants enchants;
    private EshopPermissionSys permsys;

    public DefaultMenuGenerator(int inventorySize, EshopConfig config, EshopPermissionSys permsys) {
        this.inventorySize = inventorySize;
        this.config = config;
        // TODO: Figure out better fix of EshopEnchants.
        this.enchants = new EshopEnchants();
        this.permsys = permsys;
    }

    public Inventory mainMenu(Player p) {
        Inventory inv = p.getServer().createInventory(p, inventorySize, config.getMenuName());
        generateMainMenu(p, inv);

        return inv;
    }

    public Inventory enchantMenu(Player p, ItemStack item, Map<String, String[]> playerLevels) {
        return generateEnchantMenu(p, item, playerLevels);
    }

    private void generateMainMenu(Player p, Inventory inv) {
        List<ItemStack> enchantList = enchants.getEnchantList();
        List<ItemStack> itemList = new ArrayList<>();

        for (ItemStack item : enchantList) {
            if (permsys.hasEnchantPermission(p, item)) {
                itemList.add(item);
            }
        }

        inv.setContents(itemList.toArray(new ItemStack[itemList.size()]));
    }

    private Inventory generateEnchantMenu(Player p, ItemStack item, Map<String, String[]> playerLevels) {
        Inventory inv = p.getServer().createInventory(p, inventorySize, config.getMenuName());

        Enchantment enchantment = item.getEnchantments().keySet().toArray(new Enchantment[1])[0];
        //String name = item.getItemMeta().getDisplayName();
        List<ItemStack> itemList = new ArrayList<>();

        // Generate the correct items for the player.
        // Based on permissions or OP status.
        String[] enchantLevels = config.getEnchantLevels(enchantment);
        List<String> levels = new ArrayList<>();

        for (String enchantLevel : enchantLevels) {
            enchantLevel = enchantLevel.substring(5);
            int level = Integer.parseInt(enchantLevel);
            if (permsys.hasEnchantPermission(p, enchantment, level)) {
                ItemStack tmp = item.clone();
                ItemMeta meta = tmp.getItemMeta();
                List<String> lores = new ArrayList<>();
                lores.add(ChatColor.GOLD + "Level: " + level);
                if (!(config.getEconomy() instanceof NullPayment)) {
                    int price = config.getPrice(enchantment, level);
                    lores.add(ChatColor.GREEN + "Price: " + price);
                }
                meta.setLore(lores);
                tmp.setItemMeta(meta);

                itemList.add(tmp);
                Main.debug(tmp.toString()+tmp.getType().toString()); //debug
                levels.add(enchantLevel);
            }
        }

        // Put the generated item list in the inventory
        inv.setContents(itemList.toArray(new ItemStack[itemList.size()]));

        // Generate and insert a back button
        ItemStack backitem = new ItemStack(Material.EMERALD);
        ItemMeta meta = backitem.getItemMeta();
        meta.setDisplayName("Go back");
        backitem.setItemMeta(meta);
        inv.setItem(27, backitem);

        playerLevels.put(p.getName(), levels.toArray(new String[levels.size()]));
        return inv;
    }
}
