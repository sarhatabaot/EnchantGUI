package me.tychsen.enchantgui.config;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import com.github.sarhatabaot.kraken.core.config.ConfigFile;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import me.tychsen.enchantgui.EnchantGUIPlugin;
import me.tychsen.enchantgui.economy.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class EShopConfig extends ConfigFile<EnchantGUIPlugin> {
    private YamlDocument newConfig;
    private static PaymentStrategy economy;

    public EShopConfig() {
        super(EnchantGUIPlugin.getInstance(), "", "config.yml", "");
        saveDefaultConfig();


    }

    public void createAndLoad() {
        try {
            this.newConfig = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"),
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build());
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to load config.yml");
        }
    }

    public void reload() {
        try {
            this.newConfig.reload();
        } catch (IOException e) {
            this.plugin.getLogger().severe("Failed to reload config.yml");
        }
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
                case "disable" -> setEconomy(new NullPayment());
                default -> {
                    final Material possibleMaterial = checkMaterialCurrency();
                    if(possibleMaterial == Material.AIR) {
                        setEconomy(new NullPayment());
                    } else {
                        setEconomy(new MaterialPayment(possibleMaterial));
                    }

                }
            }
        }
        return economy;
    }

    private Material checkMaterialCurrency() {
        final String paymentType = getPaymentType();
        if(paymentType.startsWith("material")) {
            final String possibleMaterial = paymentType.split(":")[1];
            return Material.matchMaterial(possibleMaterial.toUpperCase());
        }

        EnchantGUIPlugin.getInstance().getLogger().warning(() -> "Could not find matching material.");
        return Material.AIR;
    }

    public String getLanguage() {
        return config.getString("language", "en");
    }

    public static void setEconomy(PaymentStrategy economy) {
        EShopConfig.economy = economy;
    }
}
