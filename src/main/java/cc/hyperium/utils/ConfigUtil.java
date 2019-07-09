package cc.hyperium.utils;

import cc.hyperium.Hyperium;
import com.google.gson.JsonObject;

public class ConfigUtil {

    public static boolean getOptionWithDefault(String settingName, String classPath, boolean defaultObject) {
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get(classPath).getAsJsonObject();
        boolean value;
        if (!generalJsonObject.has(settingName)) {
            System.out.println(classPath + " " + settingName + " " + defaultObject);
            generalJsonObject.addProperty(settingName, defaultObject);
            saveConfig();
        }
        value = generalJsonObject.get(settingName).getAsBoolean();
        return value;
    }

    public static double getOptionWithDefault(String settingName, String classPath, Number defaultObject) {
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get(classPath).getAsJsonObject();
        double value;
        if (!generalJsonObject.has(settingName)) {
            generalJsonObject.addProperty(settingName, defaultObject);
            saveConfig();
        }
        value = generalJsonObject.get(settingName).getAsDouble();
        return value;
    }

    public static String getOptionWithDefault(String settingName, String classPath, String defaultObject) {
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get(classPath).getAsJsonObject();
        String value;
        if (!generalJsonObject.has(settingName)) {
            generalJsonObject.addProperty(settingName, defaultObject);
            saveConfig();
        }
        value = generalJsonObject.get(settingName).getAsString();
        return value;
    }

    public static char getOptionWithDefault(String settingName, String classPath, char defaultObject) {
        JsonObject generalJsonObject = Hyperium.CONFIG.getConfig().get(classPath).getAsJsonObject();
        char value;
        if (!generalJsonObject.has(settingName)) {
            generalJsonObject.addProperty(settingName, defaultObject);
            saveConfig();
        }
        value = generalJsonObject.get(settingName).getAsCharacter();
        return value;
    }

    public static void saveConfig() {
        Hyperium.CONFIG.save();
    }

    public static void register(Object obj) {
        Hyperium.CONFIG.register(obj);
    }

}
