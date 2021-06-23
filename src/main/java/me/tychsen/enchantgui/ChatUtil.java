package me.tychsen.enchantgui;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author kangarko
 */
public class ChatUtil {
    public static void tell(CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
