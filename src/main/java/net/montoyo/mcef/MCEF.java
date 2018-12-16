package net.montoyo.mcef;

import net.montoyo.mcef.client.ClientProxy;
import net.montoyo.mcef.utilities.Log;

public class MCEF {

    public static final String VERSION = "0.9";
    public static boolean ENABLE_EXAMPLE;
    public static boolean SKIP_UPDATES;
    public static boolean WARN_UPDATES;
    public static boolean USE_FORGE_SPLASH;
    public static String FORCE_MIRROR = null;
    public static String HOME_PAGE;
    public static boolean DISABLE_GPU_RENDERING;
    public static boolean CHECK_VRAM_LEAK;

    public static MCEF INSTANCE = new MCEF();

    public static ClientProxy PROXY = new ClientProxy();

    // Called by Minecraft.run() if the ShutdownPatcher succeeded
    public static void onMinecraftShutdown() {
        Log.info("Minecraft shutdown hook called!");
        try {
            PROXY.onShutdown();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void init() {
        Log.info("Loading MCEF config...");

        SKIP_UPDATES = false;
        WARN_UPDATES = false;
        USE_FORGE_SPLASH = false;

        String mirror = "";

        ENABLE_EXAMPLE = true;
        HOME_PAGE = "https://hyperium.cc";
        DISABLE_GPU_RENDERING = true;
        CHECK_VRAM_LEAK = true;

        PROXY.onPreInit();
        PROXY.onInit();
    }

}
