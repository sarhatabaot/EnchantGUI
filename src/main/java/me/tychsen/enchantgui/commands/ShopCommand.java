package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.entity.Player;

@CommandAlias("eshop|enchantgui")
@CommandPermission("eshop.use")
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
	private String prefix;
	private MenuSystem menuSystem;

	private LocalizationManager lm = LocalizationManager.getInstance();

	public ShopCommand() {
		this.prefix = lm.getString("prefix");
		this.menuSystem = Main.getMenuSystem();
	}


	@Default
	public void onGui(final Player player){
		menuSystem.showMainMenu(player);
	}

	@CommandAlias("toggle")
	@CommandPermission("eshop.enchantingtable.toggle")
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


	@CommandAlias("reload")
	@CommandPermission("eshop.reload")
	public void onReload(final Player player){
		EShopConfig.reloadConfig(player);
		LocalizationManager.getInstance().reload(player);
		EShopShop.getInstance().reload(player);
	}

	private void tell(final Player player, final String message){
		Common.tell(player,String.format("%s %s",prefix,message));
	}
}
