package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("eshop|enchantgui")
@CommandPermission(EShopPermissionSys.USE)
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
    private final String prefix;
    private final LocalizationManager lm = EnchantGUIPlugin.getInstance().getLm();

    public ShopCommand() {
        this.prefix = lm.getLanguageString("prefix");
    }


    @Default
    public void onGui(final Player player) {
        EnchantGUIPlugin.getInstance().getShopMenu().showMainMenu(player);
    }

    @Subcommand("toggle")
    @CommandPermission(EShopPermissionSys.TOGGLE)
    public void onToggle(final Player player) {
        if (!EnchantGUIPlugin.getInstance().getMainConfig().getBoolean("right-click-enchanting-table")) {
            tell(player, lm.getLanguageString("disabled-feature"));
            return;
        }

        if (EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().contains(player.getUniqueId())) {
            EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().remove(player.getUniqueId());
            tell(player, lm.getLanguageString("toggle-on"));
        } else {
            EnchantGUIPlugin.getInstance().getToggleRightClickPlayers().add(player.getUniqueId());
            tell(player, lm.getLanguageString("toggle-off"));
        }
    }


    @Subcommand("reload")
    @CommandPermission(EShopPermissionSys.RELOAD)
    public void onReload(final CommandSender player) {
        EnchantGUIPlugin.getInstance().onReload();
        EnchantGUIPlugin.getInstance().getLm().reload(player);
        EnchantGUIPlugin.getInstance().getShopMenu().reload();
        EnchantGUIPlugin.getInstance()
                .getLogger()
                .info(() -> "%s %s using: %s".formatted(getName(),
                        EnchantGUIPlugin.getInstance().getDescription().getVersion(),
                        EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().name()
                ));
    }

    private void tell(final Player player, final String message) {
        ChatUtil.sendMessage(player, String.format("%s %s", prefix, message));
    }
}
