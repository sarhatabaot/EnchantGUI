package me.tychsen.enchantgui.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
/**
 * @author kangarko
 */
public class Common {

    public static void tell(CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String prependLanguage(String language) {
        return "localization_"+language+".yml";
    }
}
