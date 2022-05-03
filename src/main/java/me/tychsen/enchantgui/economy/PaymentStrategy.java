package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PaymentStrategy {
    /**
     * @param player The player
     * @param amount The amount
     * @return If the amount was successfully withdrawn.
     */
    boolean withdraw(@NotNull Player player, int amount);
    /**
     * @param player The player
     * @param amount The amount
     * @return If the amount was successfully withdrawn.
     */
    boolean withdraw(@NotNull Player player, double amount);

    /**
     * @param player The player
     * @param amount The amount
     * @return If the player has sufficient funds.
     */
    boolean hasSufficientFunds(@NotNull Player player, int amount);
    /**
     * @param player The player
     * @param amount The amount
     * @return If the player has sufficient funds.
     */
    boolean hasSufficientFunds(@NotNull Player player, double amount);
    /**
     * @return The name of the PaymentStrategy
     */
    String name();

    String getCurrency();
}
