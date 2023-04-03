package me.tychsen.enchantgui.economy;

import me.tychsen.enchantgui.EnchantGUIPlugin;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public class PlayerPointsPayment implements PaymentStrategy {
    private final PlayerPointsAPI ppApi;
    
    public PlayerPointsPayment() {
        this.ppApi = EnchantGUIPlugin.getInstance().getPpApi();
    }
    
    @Override
    public boolean withdraw(final @NotNull Player player, final int amount) {
        if (!hasSufficientFunds(player, amount))
            return false;
        
        return ppApi.take(player.getUniqueId(), amount);
    }
    
    @Override
    public boolean hasSufficientFunds(final @NotNull Player player, final int amount) {
        return ppApi.look(player.getUniqueId()) >= amount;
    }
    
    @Override
    public boolean withdraw(@NotNull final Player player, final double amount) {
        return withdraw(player, (int) amount);
    }
    
    @Override
    public boolean hasSufficientFunds(@NotNull final Player player, final double amount) {
        return hasSufficientFunds(player, (int) amount);
    }
    
    @Override
    public String name() {
        return "PlayerPointsPayment";
    }
    
    @Override
    public String getCurrency() {
        return "pp";
    }
}
