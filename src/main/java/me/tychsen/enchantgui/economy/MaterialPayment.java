package me.tychsen.enchantgui.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MaterialPayment implements PaymentStrategy {
    private final Material material;

    public MaterialPayment(Material material) {
        this.material = material;
    }

    @Override
    public boolean withdraw(@NotNull Player player, int amount) {
        if(!hasSufficientFunds(player,amount)) return false;

        //do stuff
        return true;
    }

    @Override
    public boolean withdraw(@NotNull Player player, double amount) {
        return withdraw(player, (int) amount);
    }

    //todo not precise.
    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        final int count = Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> itemStack.getType() == material)
                .mapToInt(ItemStack::getAmount)
                .sum();

        return count >= amount;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, double amount) {
        return hasSufficientFunds(player, (int) amount);
    }

    @Override
    public String name() {
        return "MaterialPayment";
    }

    @Override
    public String getCurrency() {
        return material.name().toLowerCase();
    }
}
