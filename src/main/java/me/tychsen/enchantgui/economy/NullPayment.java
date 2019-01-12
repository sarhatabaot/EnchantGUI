package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;

/**
 * Used to disable payment.
 */
public class NullPayment implements PaymentStrategy {
    @Override
    public boolean withdraw(Player p, int amount) {
        return true;
    }

    @Override
    public boolean hasSufficientFunds(Player p, int amount) {
        return true;
    }
}
