package cc.hyperium.utils.mods;

import cc.hyperium.Hyperium;

public class AddonCheckerUtil {

    public static boolean isUsingOptifine() {
        return isUsingAddon("optifine.OptiFineTweaker");
    }

    // approved addons
    public static boolean isUsingQuickplay() {
        return isUsingAddon("co.bugg.quickplay.Quickplay");
    }

    public static boolean isUsingMediaMod() {
        return isUsingAddon("me.conorthedev.mediamod.MediaMod");
    }

    public static boolean isUsingParticleMod() {
        return isUsingAddon("net.chachy.particlemod.ParticleMod");
    }

    public static boolean isUsingArrowTrails() {
        return isUsingAddon("dev.asbyth.arrowtrail.ArrowTrail");
    }

    // non-approved addons

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
