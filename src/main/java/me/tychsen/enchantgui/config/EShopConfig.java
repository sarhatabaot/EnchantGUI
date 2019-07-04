package me.tychsen.enchantgui.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.economy.MoneyPayment;
import me.tychsen.enchantgui.economy.NullPayment;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.economy.XPPayment;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.util.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

/**
 * TODO: make this a static accessor class.
 */
public class EShopConfig {
    @Setter
    @Getter
    private static EShopConfig instance;
    @Setter
    private static FileConfiguration config;
    private PaymentStrategy economy;

    public EShopConfig() {
        setInstance(this);
        setConfig(Main.getInstance().getConfig());
    }

    public static boolean getIgnoreItemType(){
        return config.getBoolean("ignore-itemtype");
    }

    public static boolean getDebug(){
        return config.getBoolean("debug");
    }

    public static int getPrice(Enchantment enchantment, int level) {
        String path = enchantment.getKey().toString().toLowerCase() + ".level" + level;
        path = path.split(":")[1];
        return config.getInt(path);
    }

    public static boolean getBoolean(String path){
        return config.getBoolean(path);
    }

    public static String getMenuName() {
        String path = "menu-name";
        if (config.contains(path) && config.isSet(path) && config.isString(path)) {
            if (config.getString(path).length() > 32) {
                return config.getString(path).substring(0, 32);
            } else {
                return config.getString(path);
            }
        } else {
            return "EnchantGUI";
        }
    }

    public static boolean getShowPerItem(){
        return config.getBoolean("show-per-item");
    }

    public void reloadConfig(CommandSender sender) {
        LocalizationManager lm = LocalizationManager.getInstance();
        if (sender.isOp() || sender.hasPermission("eshop.admin")) {
            Main.getInstance().reloadConfig();
            setConfig(Main.getInstance().getConfig());
            economy = null;
            Common.tell(sender,DefaultMenuSystem.START + lm.getString("config-reloaded"));
        } else {
            Common.tell(sender,DefaultMenuSystem.START + lm.getString("no-permission"));
        }
    }

    public String[] getEnchantLevels(Enchantment enchantment) {
        String path = enchantment.getKey().toString().toLowerCase();
        path = path.split(":")[1];
        Main.debug(path);
        Map<String, Object> enchantMap = config.getConfigurationSection(path).getValues(false);
        String[] enchantLevels = new String[enchantMap.size()];

        int position = 0;
        for (Map.Entry<String, Object> entry : enchantMap.entrySet()) {
            enchantLevels[position] = entry.getKey();
            position++;
        }

        return enchantLevels;
    }

    public String getEconomyCurrency(){
        switch (config.getString("payment-currency").toLowerCase()) {
            case "money":
                return Main.getEconomy().currencyNameSingular();
            case "xp":
                return "levels";
            default:
                return "";
        }
    }

    public PaymentStrategy getEconomy() {
        if (economy == null) {
            switch (config.getString("payment-currency").toLowerCase()) {
                case "money":
                    economy = new MoneyPayment();
                    break;
                case "xp":
                    economy = new XPPayment();
                    break;
                default:
                    economy = new NullPayment();
                    break;
            }
        }
        return economy;
    }

}
