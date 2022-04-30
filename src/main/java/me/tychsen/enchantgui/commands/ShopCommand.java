package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.Enchants;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eshop|enchantgui")
@CommandPermission(EShopPermissionSys.USE)
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
	private final String prefix;
	private final MenuSystem menuSystem;

	private final LocalizationManager lm = Main.getInstance().getLm();

	public ShopCommand() {
		this.prefix = lm.getLanguageString("prefix");
		this.menuSystem = Main.getMenuSystem();
	}


	@Default
	public void onGui(final Player player){
		menuSystem.showMainMenu(player);
	}

	@Subcommand("toggle")
	@CommandPermission(EShopPermissionSys.TOGGLE)
	public void onToggle(final Player player){
		if(!Main.getInstance().getMainConfig().getBoolean("right-click-enchanting-table")){
			tell(player,lm.getLanguageString("disabled-feature"));
			return;
		}

		if(Main.getToggleRightClickPlayers().contains(player.getUniqueId())){
			Main.getToggleRightClickPlayers().remove(player.getUniqueId());
			tell(player,lm.getLanguageString("toggle-on"));
		}
		else {
			Main.getToggleRightClickPlayers().add(player.getUniqueId());
			tell(player,lm.getLanguageString("toggle-off"));
		}
	}


	@Subcommand("reload")
	@CommandPermission(EShopPermissionSys.RELOAD)
	public void onReload(final CommandSender player){
		Main.getInstance().onReload();
		Main.getInstance().getLm().reload(player);
		Main.getMenuSystem().reload();
		Main.getInstance().getLogger().info(() -> "%s %s using: %s".formatted(getName(),Main.getInstance().getDescription().getVersion(),Main.getInstance().getMainConfig().getPaymentStrategy().name()));
	}

	private void tell(final Player player, final String message){
		ChatUtil.sendMessage(player,String.format("%s %s",prefix,message));
	}
}
