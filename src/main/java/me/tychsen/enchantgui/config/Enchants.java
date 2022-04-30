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

public class Enchants {
    private List<ItemStack> enchantList;

    public Enchants() {
        enchantList = new ArrayList<>();
        createEnchantList();
    }

    public List<ItemStack> getEnchantList() {
        return enchantList;
    }

    public void reload() {
        enchantList = new ArrayList<>();
        createEnchantList();
    }

    private Material matchEnchant(@NotNull String enchantment) {
        return Material.matchMaterial(Main.getInstance().getLm().getActiveShopFile().getString("shop.enchants." + enchantment));
    }

    /**
     * Generates the list of all enchants with their Enchantment,
     * Material and a display name.
     */
    private void createEnchantList() {
        LocalizationManager lm = Main.getInstance().getLm();
        addItem(Enchantment.ARROW_DAMAGE, matchEnchant("power"), lm.getLanguageString("enchant.power"));
        addItem(Enchantment.ARROW_FIRE, matchEnchant("flame"), lm.getLanguageString("enchant.flame"));
        addItem(Enchantment.ARROW_INFINITE, matchEnchant("infinity"), lm.getLanguageString("enchant.infinity"));
        addItem(Enchantment.ARROW_KNOCKBACK, matchEnchant("punch"), lm.getLanguageString("enchant.punch"));
        addItem(Enchantment.DAMAGE_ALL, matchEnchant("sharpness"), lm.getLanguageString("enchant.sharpness"));
        addItem(Enchantment.DAMAGE_ARTHROPODS, matchEnchant("bane_of_arthropods"), lm.getLanguageString("enchant.bane_of_arthropods"));
        addItem(Enchantment.DAMAGE_UNDEAD, matchEnchant("smite"), lm.getLanguageString("enchant.smite"));
        addItem(Enchantment.DEPTH_STRIDER, matchEnchant("depth_strider"), lm.getLanguageString("enchant.depth_strider"));
        addItem(Enchantment.DIG_SPEED, matchEnchant("efficiency"), lm.getLanguageString("enchant.efficiency"));
        addItem(Enchantment.DURABILITY, matchEnchant("unbreaking"), lm.getLanguageString("enchant.unbreaking"));
        addItem(Enchantment.FIRE_ASPECT, matchEnchant("fire_aspect"), lm.getLanguageString("enchant.fire_aspect"));
        addItem(Enchantment.KNOCKBACK, Material.DIAMOND_SWORD, lm.getLanguageString("enchant.knockback"));
        addItem(Enchantment.LOOT_BONUS_BLOCKS, matchEnchant("fortune"), lm.getLanguageString("enchant.fortune"));
        addItem(Enchantment.LOOT_BONUS_MOBS, matchEnchant("looting"), lm.getLanguageString("enchant.looting"));
        addItem(Enchantment.LUCK, matchEnchant("luck_of_the_sea"), lm.getLanguageString("enchant.luck_of_the_sea"));
        addItem(Enchantment.LURE, matchEnchant("lure"), lm.getLanguageString("enchant.lure"));
        addItem(Enchantment.OXYGEN, matchEnchant("respiration"), lm.getLanguageString("enchant.respiration"));
        addItem(Enchantment.PROTECTION_ENVIRONMENTAL, matchEnchant("protection"), lm.getLanguageString("enchant.protection"));
        addItem(Enchantment.PROTECTION_EXPLOSIONS, matchEnchant("blast_protection"), lm.getLanguageString("enchant.blast_protection"));
        addItem(Enchantment.PROTECTION_FALL, matchEnchant("feather_falling"), lm.getLanguageString("enchant.feather_falling"));
        addItem(Enchantment.PROTECTION_FIRE, matchEnchant("fire_protection"), lm.getLanguageString("enchant.fire_protection"));
        addItem(Enchantment.PROTECTION_PROJECTILE, matchEnchant("projectile_protection"), lm.getLanguageString("enchant.projectile_protection"));
        addItem(Enchantment.SILK_TOUCH, matchEnchant("silk_touch"), lm.getLanguageString("enchant.silk_touch"));
        addItem(Enchantment.THORNS, matchEnchant("thorns"), lm.getLanguageString("enchant.thorns"));
        addItem(Enchantment.WATER_WORKER, matchEnchant("aqua_affinity"), lm.getLanguageString("enchant.aqua_affinity"));
        addItem(Enchantment.FROST_WALKER, matchEnchant("frost_walker"), lm.getLanguageString("enchant.frost_walker"));
        addItem(Enchantment.MENDING, matchEnchant("mending"), lm.getLanguageString("enchant.mending"));
        addItem(Enchantment.SWEEPING_EDGE, matchEnchant("sweeping"), lm.getLanguageString("enchant.sweeping"));
        addItem(Enchantment.CHANNELING, matchEnchant("channeling"), lm.getLanguageString("enchant.channeling"));
        addItem(Enchantment.IMPALING, matchEnchant("impaling"), lm.getLanguageString("enchant.impaling"));
        addItem(Enchantment.LOYALTY, matchEnchant("loyalty"), lm.getLanguageString("enchant.loyalty"));
        addItem(Enchantment.RIPTIDE, matchEnchant("riptide"), lm.getLanguageString("enchant.riptide"));

        addItem(Enchantment.PIERCING, matchEnchant("piercing"), lm.getLanguageString("enchant.piercing"));
        addItem(Enchantment.MULTISHOT, matchEnchant("multishot"), lm.getLanguageString("enchant.multishot"));
        addItem(Enchantment.QUICK_CHARGE, matchEnchant("quick_charge"), lm.getLanguageString("enchant.quick_charge"));

        addItem(Enchantment.SOUL_SPEED, matchEnchant("soul_speed"), lm.getLanguageString("enchant.soul_speed"));
    }

    /**
     * Add item to enchant list
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
