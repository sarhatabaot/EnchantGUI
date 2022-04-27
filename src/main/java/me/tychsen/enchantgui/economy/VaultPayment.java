package me.tychsen.enchantgui.economy;

import me.tychsen.enchantgui.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultPayment implements PaymentStrategy {
    private final Economy econ;
    private final Main plugin;

    @Override
    public String name() {
        return "MoneyPayment";
    }

    public VaultPayment() {
        this.plugin = Main.getInstance();
        if (!setupEconomy()) {
            plugin.getLogger().severe("Dependency (Vault) not found. Disabling the plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        this.econ = Main.getEconomy();
    }

    public boolean withdraw(@NotNull Player player, int amount) {
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
        Main.setEconomy(rsp.getProvider());
        return Main.getEconomy() != null;
    }

    @Override
    public boolean hasSufficientFunds(@NotNull Player player, int amount) {
        return econ.has(player, amount);
    }
}
