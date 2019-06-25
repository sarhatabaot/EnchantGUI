package me.tychsen.enchantgui.commands;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public abstract class PlayerCommand extends Command {
    private Player player;

    @Setter(value= AccessLevel.PROTECTED)
    private String prefix;

    protected PlayerCommand(String name) {
        super(name);
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender,@NotNull String commandLabel,@NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Common.tell(sender, LocalizationManager.getInstance().getString("command-from-console"));
            return false;
        }

        this.player = (Player) sender;

        try {
            run(player, args);
        } catch (final ReturnedCommandException ex) {
            final String tellMessage = ex.tellMessage;

            tell(tellMessage);
        }

        return false;
    }

    protected abstract void run(Player player, String[] args);

    protected void returnTell(String message) {
        throw new ReturnedCommandException(message);
    }

    protected void tell(String message) {
        Common.tell(player, (prefix != null ? "&8[&7" + prefix + "&8] " : "") + "&7" + message);
    }

    @RequiredArgsConstructor
    private final class ReturnedCommandException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private final String tellMessage;
    }
}
