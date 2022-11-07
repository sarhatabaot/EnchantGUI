package me.tychsen.enchantgui.economy;

import me.tychsen.enchantgui.EnchantGUIPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultPayment implements PaymentStrategy {
    private Economy econ;
    private final EnchantGUIPlugin plugin;

    @Override
    public String name() {
        return "MoneyPayment";
    }

    public VaultPayment() {
        this.plugin = EnchantGUIPlugin.getInstance();
        if (!setupEconomy()) {
            plugin.getLogger().severe("Dependency (Vault) not found. Disabling the plugin!");
            plugin.getLogger().warning("Please install vault and restart your server.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    public boolean withdraw(@NotNull Player player, int amount) {
        if(!hasSufficientFunds(player,amount))
            return false;
        return econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean withdraw(@NotNull final Player player, final double amount) {
        if(!hasSufficientFunds(player, amount))
            return false;
        return econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("could not find vault");
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().severe("could not find Economy.class");
            return false;
        }

        econ = rsp.getProvider();
        return true;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        return econ.has(player, amount);
    }

    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return econ.has(player, amount);
    }

    @Override
    public String getCurrency() {
        return econ.currencyNameSingular().isEmpty() ? "$" : econ.currencyNameSingular();
    }
}
