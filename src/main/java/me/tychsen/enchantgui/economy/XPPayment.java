package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by denni on 26/01/2016.
 */
public class XPPayment implements PaymentStrategy {
    @Override
    public String name() {
        return "XPPayment";
    }

    @Override
    public boolean withdraw(@NotNull Player p, int amount) {
        if (p.getLevel() >= amount) {
            p.giveExpLevels(-amount);
            return true;
        }

        return false;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player p, int amount) {
        return (p.getLevel() >= amount);
    }
}
