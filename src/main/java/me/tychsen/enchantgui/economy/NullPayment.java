package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;

/**
 * Used to disable payment.
 */
public class NullPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "NullPayment";
    }

    @Override
    public boolean withdraw(Player player, int amount) {
        return true;
    }

    @Override
    public boolean hasSufficientFunds(Player player, int amount) {
        return true;
    }
}
