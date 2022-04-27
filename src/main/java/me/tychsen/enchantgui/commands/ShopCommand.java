package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopEnchants;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.ChatUtil;
import me.tychsen.enchantgui.permissions.EShopPermissionSys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eshop|enchantgui")
@CommandPermission(EShopPermissionSys.USE)
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
	private final String prefix;
	private final MenuSystem menuSystem;

	private final LocalizationManager lm = LocalizationManager.getInstance();

	public ShopCommand() {
		this.prefix = lm.getString("prefix");
		this.menuSystem = Main.getMenuSystem();
	}


	@Default
	public void onGui(final Player player){
		menuSystem.showMainMenu(player);
	}

	@Subcommand("toggle")
	@CommandPermission(EShopPermissionSys.TOGGLE)
	public void onToggle(final Player player){
		if(!EShopConfig.getBoolean("right-click-enchanting-table")){
			tell(player,lm.getString("disabled-feature"));
			return;
		}

		if(Main.getToggleRightClickPlayers().contains(player.getUniqueId())){
			Main.getToggleRightClickPlayers().remove(player.getUniqueId());
			tell(player,lm.getString("toggle-on"));
		}
		else {
			Main.getToggleRightClickPlayers().add(player.getUniqueId());
			tell(player,lm.getString("toggle-off"));
		}
	}


	@Subcommand("reload")
	@CommandPermission(EShopPermissionSys.RELOAD)
	public void onReload(final CommandSender player){
		EShopConfig.reloadConfig(player);
		LocalizationManager.getInstance().reload(player);
		Main.setMenuSystem(new DefaultMenuSystem());
		Main.getMenuSystem().getMenuGenerator().setShopEnchants(new EShopEnchants());
		EShopShop.getInstance().reload(player);
		Main.getInstance().onReload();
		Main.getInstance().getLogger().info(getName() + " " + Main.getInstance().getDescription().getVersion() + " using: "+ EShopConfig.getPaymentStrategy().name());
	}

	private void tell(final Player player, final String message){
		ChatUtil.tell(player,String.format("%s %s",prefix,message));
	}
}
