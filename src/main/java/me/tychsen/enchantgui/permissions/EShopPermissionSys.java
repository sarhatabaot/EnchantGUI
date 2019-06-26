package me.tychsen.enchantgui.permissions;

import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EShopPermissionSys {
    public boolean hasEnchantPermission(Player p, ItemStack item) {
        if (p.isOp()) return true;

        Map<Enchantment, Integer> enchants = item.getEnchantments();
        if (enchants.size() > 1) throw new TooManyEnchantmentsException("Item has more than one enchant!");

        Enchantment ench = enchants.keySet().toArray(new Enchantment[1])[0];
        String base = "eshop.enchants.";
        String name = (ench.getKey().toString().toLowerCase()).split(":")[1];
        String perm = base + name;

        return p.hasPermission(perm) || p.hasPermission(base + "all");
    }

    public boolean hasEnchantPermission(Player p, Enchantment ench, int level) {
        if (p.isOp()) return true;

        String base = "eshop.enchants.";
        String enchName = (ench.getKey().toString().toLowerCase()).split(":")[1];
        String perm = base + enchName + "." + level;

        return p.hasPermission(perm) || p.hasPermission(base + enchName + ".all") || p.hasPermission(base + "all");
    }

    public boolean hasUsePermission(Player p) {
        if (p.isOp()) return true;

        String base = "eshop.";
        String perm = base + "use";

        return  p.hasPermission(perm);
    }
    @RequiredArgsConstructor
    private final class TooManyEnchantmentsException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private final String tellMessage;
    }
}
