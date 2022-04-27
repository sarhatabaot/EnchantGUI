package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Used to disable payment.
 */
public class NullPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "NullPayment";
    }

    @Override
    public boolean withdraw(@NotNull Player player, int amount) {
        return true;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        return true;
    }
}
