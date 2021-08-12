package me.tychsen.enchantgui.economy;

import org.bukkit.entity.Player;

public interface PaymentStrategy {
    boolean withdraw(Player p, int amount);
    boolean hasSufficientFunds(Player p, int amount);
    String name();
}
