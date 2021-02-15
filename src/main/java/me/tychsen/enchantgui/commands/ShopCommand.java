package me.tychsen.enchantgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.tychsen.enchantgui.EnchantGUI;
import me.tychsen.enchantgui.config.EShopConfig;
import me.tychsen.enchantgui.config.EShopShop;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.menu.MenuSystem;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.entity.Player;

@CommandAlias("eshop|enchantgui")
@CommandPermission("eshop.use")
@Description("Command for the EnchantGUI plugin.")
public class ShopCommand extends BaseCommand {
	private final String prefix;
	private final MenuSystem menuSystem;

	private final LocalizationManager lm = LocalizationManager.getInstance();

	public ShopCommand() {
		this.prefix = lm.getString("prefix");
		this.menuSystem = EnchantGUI.getMenuSystem();
	}


	@Default
	public void onGui(final Player player){
		menuSystem.showMainMenu(player);
	}

	@Subcommand("toggle")
	@CommandPermission("eshop.enchantingtable.toggle")
	public void onToggle(final Player player){
		if(!EShopConfig.getBoolean("right-click-enchanting-table")){
			tell(player,lm.getString("disabled-feature"));
			return;
		}

		if(EnchantGUI.getToggleRightClickPlayers().contains(player.getUniqueId())){
			EnchantGUI.getToggleRightClickPlayers().remove(player.getUniqueId());
			tell(player,lm.getString("toggle-on"));
		}
		else {
			EnchantGUI.getToggleRightClickPlayers().add(player.getUniqueId());
			tell(player,lm.getString("toggle-off"));
		}
	}


	@Subcommand("reload")
	@CommandPermission("eshop.reload")
	public void onReload(final Player player){
		EShopConfig.reloadConfig(player);
		reloadLocalizationManager(player);
		reloadMenuSystem();
		EShopShop.getInstance().reload(player);
	}

	private void reloadMenuSystem(){
		EnchantGUI.setMenuSystem(null);
		EnchantGUI.setMenuSystem(new DefaultMenuSystem());
	}

	private void reloadLocalizationManager(final Player player){
		LocalizationManager.setInstance(null);
		new LocalizationManager(Common.prependLanguage(
				EShopConfig.getLang()));
		LocalizationManager.getInstance().reload(player);
	}

	private void tell(final Player player, final String message){
		Common.tell(player,String.format("%s %s",prefix,message));
	}
}
