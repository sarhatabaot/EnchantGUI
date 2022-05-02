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
    public boolean withdraw(@NotNull Player player, int amount) {
        if (hasSufficientFunds(player,amount)) {
            player.giveExpLevels(-amount);
            return true;
        }

        return false;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        return (player.getLevel() >= amount);
    }

    @Override
    public String getCurrency() {
        return "levels";
    }
}
