package cc.hyperium.addons.customrp.utils;

import net.minecraft.client.Minecraft;

public class EUtils {

    private static String username, eIfiedUsername, allEUsername;

    static {
        username = Minecraft.getMinecraft().getSession().getUsername();

        eIfiedUsername = username
                .replaceAll("a|i|o|u", "e")
                .replaceAll("A|E|I|O|U", "e");

        allEUsername = username
                .replaceAll("[A-Z]", "e")
                .replaceAll("[a-z]", "e")
                .replaceAll("[0-9]", "e");
    }

    public static String getUsername() {
        return username;
    }

    public static String geteIfiedUsername() {
        return eIfiedUsername;
    }

    public static String getAllEUsername() {
        return allEUsername;
    }
}
