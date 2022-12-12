package me.tychsen.enchantgui.permissions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EShopPermissionSys {
    public static final String USE = "eshop.use";
    public static final String ENCHANTING_TABLE = "eshop.enchantingtable";
    public static final String TOGGLE = "eshop.enchantingtable.toggle";
    public static final String RELOAD = "eshop.reload";

    private static final String BASE = "eshop.enchants.";

    public static boolean hasEnchantPermission(final @NotNull Player player, final ItemStack item) {
        if (player.isOp()) return true;

        Map<Enchantment, Integer> enchants = item.getEnchantments();
        if (enchants.size() > 1) {
            throw new TooManyEnchantmentsException("Item has more than one enchant!");
            //This should never happen.
        }

        Enchantment enchantment = enchants.keySet().toArray(new Enchantment[1])[0];
        String name = (enchantment.getKey().toString().toLowerCase()).split(":")[1];
        String perm = getEnchantmentPermission(name);
        return player.hasPermission(perm) || player.hasPermission(BASE + "all");
    }

    public static boolean hasEnchantPermission(final @NotNull Player player, final Enchantment enchantment, int level) {
        if (player.isOp()) return true;
        String enchantmentName = (enchantment.getKey().toString().toLowerCase()).split(":")[1];
        String perm = getEnchantmentLevelPermission(enchantmentName, level);

        return player.hasPermission(perm) || player.hasPermission(BASE + enchantmentName + ".all") || player.hasPermission(BASE + "all");
    }

    public static boolean hasUsePermission(@NotNull Player player) {
        if (player.isOp()) return true;
        return player.hasPermission(EShopPermissionSys.USE);
    }

    @Contract(pure = true)
    private static @NotNull String getEnchantmentPermission(final String name) {
        return BASE + name;
    }

    @Contract(pure = true)
    private static @NotNull String getEnchantmentLevelPermission(final String name, int level) {
        return getEnchantmentPermission(name) + "." + level;
    }

}
