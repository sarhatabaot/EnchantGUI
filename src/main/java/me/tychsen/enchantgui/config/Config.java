package me.tychsen.enchantgui.config;

/**
 * @author sarhatabaot
 */
public class Config {
    private static String FILE_NAME;

    public Config(String FILE_NAME){
        Config.setFileName(FILE_NAME);
    }

    public static String getFileName() {
        return FILE_NAME;
    }

    public static void setFileName(String fileName) {
        FILE_NAME = fileName;
    }

    //TODO: Abstract, interface
}
