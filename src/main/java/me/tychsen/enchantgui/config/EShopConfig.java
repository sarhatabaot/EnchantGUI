package me.tychsen.enchantgui.config;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import com.github.sarhatabaot.kraken.core.config.ConfigFile;
import lombok.Setter;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.economy.NullPayment;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.economy.PlayerPointsPayment;
import me.tychsen.enchantgui.economy.VaultPayment;
import me.tychsen.enchantgui.economy.XPPayment;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EShopConfig extends ConfigFile<EnchantGUIPlugin> {

    @Setter
    private static PaymentStrategy economy;

    public EShopConfig() {
        super(EnchantGUIPlugin.getInstance(), "", "config.yml", "");
        saveDefaultConfig();
    }


    public boolean getIgnoreItemType() {
        return config.getBoolean("ignore-itemtype");
    }

    public boolean getDebug() {
        return config.getBoolean("debug");
    }

    public double getPrice(@NotNull Enchantment enchantment, int level) {
        String path = enchantment.getKey().toString().toLowerCase() + ".level" + level;
        path = path.split(":")[1];
        return config.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public String getMenuName() {
        var path = "menu-name";
        final String defaultName = "EnchantGUI";

        if (config.getString(path, defaultName).length() > 32) {
            return config.getString(path, defaultName).substring(0, 32);
        }

        return config.getString(path, defaultName);


    }

    public boolean getShowPerItem() {
        return config.getBoolean("show-per-item");
    }

    public void reloadConfig(@NotNull CommandSender sender) {
        reloadConfig();
        ChatUtil.sendMessage(sender,  EnchantGUIPlugin.getInstance().getLm().getPrefix() + " " + EnchantGUIPlugin.getInstance().getLm().getLanguageString("config-reloaded"));
    }


    public @NotNull String[] getEnchantLevels(@NotNull Enchantment enchantment) {
        String path = enchantment.getKey().toString().toLowerCase();
        path = path.split(":")[1];
        EnchantGUIPlugin.debug(path);
        Map<String, Object> enchantMap = config.getConfigurationSection(path).getValues(false);
        String[] enchantLevels = new String[enchantMap.size()];

        var position = 0;
        for (Map.Entry<String, Object> entry : enchantMap.entrySet()) {
            enchantLevels[position] = entry.getKey();
            position++;
        }

        return enchantLevels;
    }

    public String getPaymentType() {
        return config.getString("payment-currency", "xp");
    }

    public PaymentStrategy getPaymentStrategy() {
        if (economy == null) {
            switch (getPaymentType().toLowerCase()) {
                case "money" -> setEconomy(new VaultPayment());
                case "xp" -> setEconomy(new XPPayment());
                case "playerpoints" -> setEconomy(new PlayerPointsPayment());
                default -> setEconomy(new NullPayment());
            }
        }
        return economy;
    }

    public String getLanguage() {
        return config.getString("language", "en");
    }

}
