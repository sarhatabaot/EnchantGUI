package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;

public interface PaymentStrategy {
    boolean withdraw(Player player, int amount);
    boolean hasSufficientFunds(Player player, int amount);
    String name();
}
