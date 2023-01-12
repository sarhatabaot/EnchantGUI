package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.config.Enchants;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.ShopMenu;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("eshop|enchantgui")
@CommandPermission(EShopPermissionSys.USE)
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
	private final String prefix;
	private final ShopMenu shopMenu;

	private final LocalizationManager lm = EnchantGUIPlugin.getInstance().getLm();

	public ShopCommand() {
		this.prefix = lm.getLanguageString("prefix");
		this.shopMenu = new ShopMenu(new Enchants());
	}


	@Default
	public void onGui(final Player player){
		shopMenu.showMainMenu(player);
	}

	@Subcommand("toggle")
	@CommandPermission(EShopPermissionSys.TOGGLE)
	public void onToggle(final Player player){
		if(!EnchantGUIPlugin.getInstance().getMainConfig().getBoolean("right-click-enchanting-table")){
			tell(player,lm.getLanguageString("disabled-feature"));
			return;
		}

		if(EnchantGUIPlugin.getToggleRightClickPlayers().contains(player.getUniqueId())){
			EnchantGUIPlugin.getToggleRightClickPlayers().remove(player.getUniqueId());
			tell(player,lm.getLanguageString("toggle-on"));
		}
		else {
			EnchantGUIPlugin.getToggleRightClickPlayers().add(player.getUniqueId());
			tell(player,lm.getLanguageString("toggle-off"));
		}
	}


	@Subcommand("reload")
	@CommandPermission(EShopPermissionSys.RELOAD)
	public void onReload(final CommandSender player){
		EnchantGUIPlugin.getInstance().onReload();
		EnchantGUIPlugin.getInstance().getLm().reload(player);
		this.shopMenu.reload();
		EnchantGUIPlugin.getInstance().getLogger().info(() -> "%s %s using: %s".formatted(getName(), EnchantGUIPlugin.getInstance().getDescription().getVersion(), EnchantGUIPlugin.getInstance().getMainConfig().getPaymentStrategy().name()));
	}

	private void tell(final Player player, final String message){
		ChatUtil.sendMessage(player,String.format("%s %s",prefix,message));
	}
}
