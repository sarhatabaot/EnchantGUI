package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;

/**
 * Created by denni on 26/01/2016.
 */
public class XPPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "XPPayment";
    }

    @Override
    public boolean withdraw(Player p, int amount) {
        if (p.getLevel() >= amount) {
            p.giveExpLevels(-amount);
            return true;
        }

        return false;
    }

    @Override
    public boolean hasSufficientFunds(Player p, int amount) {
        return (p.getLevel() >= amount);
    }
}
