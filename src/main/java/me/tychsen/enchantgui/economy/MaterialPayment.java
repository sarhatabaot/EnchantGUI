package me.tychsen.enchantgui.economy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class MaterialPayment implements PaymentStrategy {
    private final Material material;

    public MaterialPayment(Material material) {
        this.material = material;
    }

    @Override
    public boolean withdraw(@NotNull Player player, int amount) {
        if (!hasSufficientFunds(player, amount)) {
            return false;
        }

        // Collect matching stacks from the inventory, sorted by stack size (ascending)
        List<ItemStack> matchingStacks = Stream.of(player.getInventory().getContents())
                .filter(stack -> stack != null && stack.isSimilar(new ItemStack(material)))
                .sorted(Comparator.comparingInt(ItemStack::getAmount))
                .toList();

        int remaining = amount;

        for (ItemStack stack : matchingStacks) {
            int stackAmount = stack.getAmount();

            if (remaining <= stackAmount) {
                // Deduct the remaining amount and break
                stack.setAmount(stackAmount - remaining);
                break;
            } else {
                // Remove the entire stack and reduce the remaining amount
                remaining -= stackAmount;
                stack.setAmount(0);
            }
        }

        // Update inventory to reflect changes
        player.updateInventory();
        return true;
    }


    @Override
    public boolean withdraw(@NotNull Player player, double amount) {
        return withdraw(player, (int) amount);
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        final int count = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
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
