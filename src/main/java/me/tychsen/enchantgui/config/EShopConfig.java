package me.tychsen.enchantgui.config;

import lombok.Getter;
import lombok.Setter;
import me.tychsen.enchantgui.economy.MoneyPayment;
import me.tychsen.enchantgui.economy.NullPayment;
import me.tychsen.enchantgui.economy.PaymentStrategy;
import me.tychsen.enchantgui.economy.XPPayment;
import me.tychsen.enchantgui.localization.LocalizationManager;
import me.tychsen.enchantgui.menu.DefaultMenuSystem;
import me.tychsen.enchantgui.Main;
import me.tychsen.enchantgui.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class EShopConfig {
    @Setter
    @Getter
    private static EShopConfig instance;
    @Setter
    private static FileConfiguration config;
    @Setter
    private static PaymentStrategy economy;

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
        var path = "menu-name";
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

    public static void reloadConfig(CommandSender sender) {
        LocalizationManager lm = LocalizationManager.getInstance();
        if (sender.isOp() || sender.hasPermission("eshop.admin")) {
            Main.getInstance().reloadConfig();
            setConfig(Main.getInstance().getConfig());
            economy = null;
            ChatUtil.tell(sender,DefaultMenuSystem.PREFIX + lm.getString("config-reloaded"));
        } else {
            ChatUtil.tell(sender,DefaultMenuSystem.PREFIX + lm.getString("no-permission"));
        }
    }

    public static String[] getEnchantLevels(Enchantment enchantment) {
        String path = enchantment.getKey().toString().toLowerCase();
        path = path.split(":")[1];
        Main.debug(path);
        Map<String, Object> enchantMap = config.getConfigurationSection(path).getValues(false);
        String[] enchantLevels = new String[enchantMap.size()];

        var position = 0;
        for (Map.Entry<String, Object> entry : enchantMap.entrySet()) {
            enchantLevels[position] = entry.getKey();
            position++;
        }

        return enchantLevels;
    }

    public static String getEconomyCurrency(){
        return switch (config.getString("payment-currency").toLowerCase()) {
            case "money" -> Main.getEconomy().currencyNameSingular();
            case "xp" -> "levels";
            default -> "";
        };
    }

    public static PaymentStrategy getEconomy() {
        if (economy == null) {
            switch (config.getString("payment-currency").toLowerCase()) {
                case "money" -> setEconomy(new MoneyPayment());
                case "xp" -> setEconomy(new XPPayment());
                default -> setEconomy(new NullPayment());
            }
        }
        return economy;
    }

    public static String getLanguage() {
        return config.getString("language","en");
    }

}
