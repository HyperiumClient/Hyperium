package cc.hyperium.addons.customrp.utils;

import net.minecraft.client.Minecraft;

public class EUtils {

    private static String username, eIfiedUsername, allEUsername;

    static {
        username = Minecraft.getMinecraft().getSession().getUsername();

        eIfiedUsername = username
                .replaceAll("a|i|o|u", "e")
                .replaceAll("A|I|O|U", "E");

        allEUsername = username
                .replaceAll("[A-Z]", "E")
                .replaceAll("[a-z]", "e");
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
