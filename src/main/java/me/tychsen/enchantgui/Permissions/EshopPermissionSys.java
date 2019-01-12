package me.tychsen.enchantgui.Permissions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EshopPermissionSys {
    public boolean hasEnchantPermission(Player p, ItemStack item) {
        if (p.isOp()) return true;

        Map<Enchantment, Integer> enchants = item.getEnchantments();
        if (enchants.size() > 1) throw new RuntimeException("Item has more than one enchant!");

        Enchantment ench = enchants.keySet().toArray(new Enchantment[1])[0];
        String base = "eshop.enchants.";
        String name = ench.getKey().getKey().toLowerCase();
        String perm = base + name;

        return p.hasPermission(perm) || p.hasPermission(base + "all");
    }

    public boolean hasEnchantPermission(Player p, Enchantment ench, int level) {
        if (p.isOp()) return true;

        String base = "eshop.enchants.";
        String enchName = ench.getKey().getKey().toLowerCase();
        String perm = base + enchName + "." + level;

        return p.hasPermission(perm) || p.hasPermission(base + enchName + ".all") || p.hasPermission(base + "all");
    }

    public boolean hasUsePermission(Player p) {
        if (p.isOp()) return true;

        String base = "eshop.";
        String perm = base + "use";

        return  p.hasPermission(perm);
    }
}
