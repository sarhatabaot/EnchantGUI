package me.tychsen.enchantgui;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author kangarko
 */
public class ChatUtil {
    public static void tell(@NotNull CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
