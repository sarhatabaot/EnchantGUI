package me.tychsen.enchantgui.config;

import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.localization.LocalizationManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EshopEnchants {
    private List<ItemStack> enchantList = new ArrayList<>();
    private EshopShop shop;

    public EshopEnchants() {
        shop = EshopShop.getInstance();
        createEnchantList();
    }

    public List<ItemStack> getEnchantList() { return enchantList; }

    private Material matchEnchant(@NotNull String enchantment){
        return Material.matchMaterial(shop.getString("shop.enchants."+enchantment));
    }

    /**
     * Generates the list of all enchants with their Enchantment,
     * Material and a display name.
     */
    private void createEnchantList() {
        LocalizationManager lm = LocalizationManager.getInstance();
        addItem(Enchantment.ARROW_DAMAGE, matchEnchant("power"), lm.getString("enchant.power"));
        addItem(Enchantment.ARROW_FIRE, matchEnchant("flame"), lm.getString("enchant.flame"));
        addItem(Enchantment.ARROW_INFINITE, matchEnchant("infinity"), lm.getString("enchant.infinity"));
        addItem(Enchantment.ARROW_KNOCKBACK, matchEnchant("punch"), lm.getString("enchant.punch"));
        addItem(Enchantment.DAMAGE_ALL, matchEnchant("sharpness"), lm.getString("enchant.sharpness"));
        addItem(Enchantment.DAMAGE_ARTHROPODS, matchEnchant("bane_of_arthropods"), lm.getString("enchant.bane_of_arthropods"));
        addItem(Enchantment.DAMAGE_UNDEAD, matchEnchant("smite"), lm.getString("enchant.smite"));
        addItem(Enchantment.DEPTH_STRIDER, matchEnchant("depth_strider"), lm.getString("enchant.depth_strider"));
        addItem(Enchantment.DIG_SPEED, matchEnchant("efficiency"), lm.getString("enchant.efficiency"));
        addItem(Enchantment.DURABILITY, matchEnchant("unbreaking"), lm.getString("enchant.unbreaking"));
        addItem(Enchantment.FIRE_ASPECT, matchEnchant("fire_aspect"), lm.getString("enchant.fire_aspect"));
        addItem(Enchantment.KNOCKBACK, Material.DIAMOND_SWORD, lm.getString("enchant.knockback"));
        addItem(Enchantment.LOOT_BONUS_BLOCKS, matchEnchant("fortune"), lm.getString("enchant.fortune"));
        addItem(Enchantment.LOOT_BONUS_MOBS, matchEnchant("looting"), lm.getString("enchant.looting"));
        addItem(Enchantment.LUCK, matchEnchant("luck_of_the_sea"), lm.getString("enchant.luck_of_the_sea"));
        addItem(Enchantment.LURE, matchEnchant("lure"), lm.getString("enchant.lure"));
        addItem(Enchantment.OXYGEN, matchEnchant("respiration"), lm.getString("enchant.respiration"));
        addItem(Enchantment.PROTECTION_ENVIRONMENTAL, matchEnchant("protection"), lm.getString("enchant.protection"));
        addItem(Enchantment.PROTECTION_EXPLOSIONS, matchEnchant("blast_protection"), lm.getString("enchant.blast_protection"));
        addItem(Enchantment.PROTECTION_FALL, matchEnchant("feather_falling"), lm.getString("enchant.feather_falling"));
        addItem(Enchantment.PROTECTION_FIRE, matchEnchant("fire_protection"), lm.getString("enchant.fire_protection"));
        addItem(Enchantment.PROTECTION_PROJECTILE, matchEnchant("projectile_protection"), lm.getString("enchant.projectile_protection"));
        addItem(Enchantment.SILK_TOUCH, matchEnchant("silk_touch"), lm.getString("enchant.silk_touch"));
        addItem(Enchantment.THORNS, matchEnchant("thorns"), lm.getString("enchant.thorns"));
        addItem(Enchantment.WATER_WORKER, matchEnchant("aqua_affinity"), lm.getString("enchant.aqua_affinity"));
        addItem(Enchantment.FROST_WALKER, matchEnchant("frost_walker"), lm.getString("enchant.frost_walker"));
        addItem(Enchantment.MENDING, matchEnchant("mending"), lm.getString("enchant.mending"));
        addItem(Enchantment.SWEEPING_EDGE,matchEnchant("sweeping"),lm.getString("enchant.sweeping"));
        addItem(Enchantment.CHANNELING,matchEnchant("channeling"),lm.getString("enchant.channeling"));
        addItem(Enchantment.IMPALING,matchEnchant("impaling"),lm.getString("enchant.impaling"));
        addItem(Enchantment.LOYALTY,matchEnchant("loyalty"),lm.getString("enchant.loyalty"));
        addItem(Enchantment.RIPTIDE,matchEnchant("riptide"),lm.getString("enchant.riptide"));
        if(!Main.getMinecraftVersion().contains("1.13")) {
            addItem(Enchantment.PIERCING, matchEnchant("piercing"), lm.getString("enchant.piercing"));
            addItem(Enchantment.MULTISHOT, matchEnchant("multishot"), lm.getString("enchant.multishot"));
            addItem(Enchantment.QUICK_CHARGE, matchEnchant("quick_charge"), lm.getString("enchant.quick_charge"));
            Main.getInstance().getLogger().info("Registered 1.14 enchantments.");
        }
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

}
