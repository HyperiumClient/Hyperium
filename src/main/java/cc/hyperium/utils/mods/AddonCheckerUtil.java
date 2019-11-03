package cc.hyperium.utils.mods;

import cc.hyperium.Hyperium;

public class AddonCheckerUtil {

    public static boolean isUsingQuickplay() {
        try {
            Class.forName("co.bugg.quickplay.Quickplay");
            Hyperium.LOGGER.info("Found Quickplay, running Quickplay calls.");
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static boolean isUsingOptifine() {
        try {
            Class.forName("optifine.OptiFineTweaker");
            Hyperium.LOGGER.info("Found Optifine, running Optifine calls.");
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    // access any named addon for addon developers
    // mainAddonClass must be the path to the "mainClass" variable in the addon
    // ex: cc.hyperium.Hyperium
    public static boolean isUsingAddon(String mainAddonClass) {
        try {
            Class.forName(mainAddonClass);
            Hyperium.LOGGER.info("Found " + mainAddonClass);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
