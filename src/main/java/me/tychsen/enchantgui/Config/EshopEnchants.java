package me.tychsen.enchantgui.Config;

import me.tychsen.enchantgui.Localization.LocalizationManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EshopEnchants {
    private List<ItemStack> enchantList = new ArrayList<>();

    public EshopEnchants() {
        createEnchantList();
    }

    public List<ItemStack> getEnchantList() { return enchantList; }

    /**
     * Generates the list of all enchants with their Enchantment,
     * Material and a display name.
     */
    private void createEnchantList() {
        LocalizationManager lm = LocalizationManager.getInstance();
        addItem(Enchantment.ARROW_DAMAGE, Material.BOW, lm.getString("enchant.power"));
        addItem(Enchantment.ARROW_FIRE, Material.BOW, lm.getString("enchant.flame"));
        addItem(Enchantment.ARROW_INFINITE, Material.BOW, lm.getString("enchant.infinity"));
        addItem(Enchantment.ARROW_KNOCKBACK, Material.BOW, lm.getString("enchant.punch"));
        addItem(Enchantment.DAMAGE_ALL, Material.DIAMOND_SWORD, lm.getString("enchant.sharpness"));
        addItem(Enchantment.DAMAGE_ARTHROPODS, Material.DIAMOND_SWORD, lm.getString("enchant.bane_of_arthropods"));
        addItem(Enchantment.DAMAGE_UNDEAD, Material.DIAMOND_SWORD, lm.getString("enchant.smite"));
        addItem(Enchantment.DEPTH_STRIDER, Material.DIAMOND_BOOTS, lm.getString("enchant.depth_strider"));
        addItem(Enchantment.DIG_SPEED, Material.DIAMOND_PICKAXE, lm.getString("enchant.efficiency"));
        addItem(Enchantment.DURABILITY, Material.DIAMOND_PICKAXE, lm.getString("enchant.durability"));
        addItem(Enchantment.FIRE_ASPECT, Material.DIAMOND_SWORD, lm.getString("enchant.fire_aspect"));
        addItem(Enchantment.KNOCKBACK, Material.DIAMOND_SWORD, lm.getString("enchant.knockback"));
        addItem(Enchantment.LOOT_BONUS_BLOCKS, Material.DIAMOND_PICKAXE, lm.getString("enchant.fortune"));
        addItem(Enchantment.LOOT_BONUS_MOBS, Material.DIAMOND_SWORD, lm.getString("enchant.looting"));
        addItem(Enchantment.LUCK, Material.FISHING_ROD, lm.getString("enchant.luck_of_the_sea"));
        addItem(Enchantment.LURE, Material.FISHING_ROD, lm.getString("enchant.lure"));
        addItem(Enchantment.OXYGEN, Material.DIAMOND_HELMET, lm.getString("enchant.respiration"));
        addItem(Enchantment.PROTECTION_ENVIRONMENTAL, Material.DIAMOND_CHESTPLATE, lm.getString("enchant.protection"));
        addItem(Enchantment.PROTECTION_EXPLOSIONS, Material.DIAMOND_CHESTPLATE, lm.getString("enchant.blast_protection"));
        addItem(Enchantment.PROTECTION_FALL, Material.DIAMOND_BOOTS, lm.getString("enchant.feather_falling"));
        addItem(Enchantment.PROTECTION_FIRE, Material.DIAMOND_CHESTPLATE, lm.getString("enchant.fire_protection"));
        addItem(Enchantment.PROTECTION_PROJECTILE, Material.DIAMOND_CHESTPLATE, lm.getString("enchant.projectile_protection"));
        addItem(Enchantment.SILK_TOUCH, Material.DIAMOND_PICKAXE, lm.getString("enchant.silk_touch"));
        addItem(Enchantment.THORNS, Material.DIAMOND_CHESTPLATE, lm.getString("enchant.thorns"));
        addItem(Enchantment.WATER_WORKER, Material.DIAMOND_HELMET, lm.getString("enchant.aqua_affinity"));
        addItem(Enchantment.FROST_WALKER, Material.DIAMOND_BOOTS, lm.getString("enchant.frost_walker"));
        addItem(Enchantment.MENDING, Material.DIAMOND_PICKAXE, lm.getString("enchant.mending"));
        // 1.13
        addItem(Enchantment.SWEEPING_EDGE,Material.DIAMOND_SWORD,lm.getString("enchant.sweeping"));

        addItem(Enchantment.CHANNELING,Material.TRIDENT,lm.getString("enchant.channeling"));
        addItem(Enchantment.IMPALING,Material.TRIDENT,lm.getString("enchant.impaling"));
        addItem(Enchantment.LOYALTY,Material.TRIDENT,lm.getString("enchant.loyalty"));
        addItem(Enchantment.RIPTIDE,Material.TRIDENT,lm.getString("enchant.riptide"));

    }

    /**
     * Add item to the enchant list
     */
    private void addItem(Enchantment type, Material mat, String displayName) {
        ItemStack item = new ItemStack(mat);
        item.addEnchantment(type, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        enchantList.add(item);
    }

    /**
     * Returns the permission name for the enchant type.
     */
    private String enchantPermName(Enchantment type) {
        String perm = "eshop.enchant.";
        perm += (type.getKey().toString().toLowerCase()).split(":")[1];
        return perm;
    }
}
