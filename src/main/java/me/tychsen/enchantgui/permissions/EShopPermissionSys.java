package me.tychsen.enchantgui.permissions;

import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serial;
import java.util.Map;

public class EShopPermissionSys {
    public static final String USE = "eshop.use";
    public static final String TOGGLE = "eshop.enchantingtable.toggle";
    public static final String RELOAD = "eshop.reload";

    private static final String BASE = "eshop.enchants.";
    public boolean hasEnchantPermission(final Player player,final ItemStack item) {
        if (player.isOp()) return true;

        Map<Enchantment, Integer> enchants = item.getEnchantments();
        if (enchants.size() > 1) throw new TooManyEnchantmentsException("Item has more than one enchant!");

        Enchantment ench = enchants.keySet().toArray(new Enchantment[1])[0];
        String name = (ench.getKey().toString().toLowerCase()).split(":")[1];
        String perm = BASE + name;

        return player.hasPermission(perm) || player.hasPermission(BASE + "all");
    }

    public boolean hasEnchantPermission(final Player player,final Enchantment enchantment, int level) {
        if (player.isOp()) return true;
        String enchantmentName = (enchantment.getKey().toString().toLowerCase()).split(":")[1];
        String perm = BASE + enchantmentName + "." + level;

        return player.hasPermission(perm) || player.hasPermission(BASE + enchantmentName + ".all") || player.hasPermission(BASE + "all");
    }

    public boolean hasUsePermission(Player p) {
        if (p.isOp()) return true;
        return  p.hasPermission(EShopPermissionSys.USE);
    }

    @RequiredArgsConstructor
    private static final class TooManyEnchantmentsException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String tellMessage;
    }
}
