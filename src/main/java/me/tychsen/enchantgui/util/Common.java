package me.tychsen.enchantgui.util;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author kangarko
 */
public class Common {

    @Setter
    @Getter
    private static JavaPlugin instance;

    public static void sendTitle(Player pl, String title, String subtitle) {
        pl.sendTitle(colorize(title), colorize(subtitle), 20, 3 * 20, 10);
    }

    public static void sendBar(Player pl, String title) {
        try {
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(colorize(title)));

        } catch (final Throwable t) {
            tell(pl, title);
        }
    }

    public static void log(String... messages) {
        for (final String message : messages)
            log(message);
    }

    public static void log(String messages) {
        tell(Bukkit.getConsoleSender(), "[" + instance.getName() + "] " + messages);
    }

    public static void tell(CommandSender toWhom, String... messages) {
        for (final String message : messages)
            tell(toWhom, message);
    }

    public static void tell(CommandSender toWhom, String message) {
        toWhom.sendMessage(colorize(message));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void registerCommand(Command command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);

        } catch(final Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getOrDefault(T nullable, T def) {
        return nullable != null ? nullable : def;
    }

    public static void runLater(int delay, Runnable task) {
        Bukkit.getScheduler().runTaskLater(instance, task, delay);
    }
}
