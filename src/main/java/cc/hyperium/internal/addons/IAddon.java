package cc.hyperium.internal.addons;

/**
 * Interface of which the main class
 * of an Addon must implement
 *
 * @author Kevin Brewster
 * @since 1.0
 */
public interface IAddon {

    /**
     * Invoked once the plugin has successfully loaded
     * {@see cc.hyperium.internal.addons.AddonMinecraftBootstrap#init}
     */
    void onLoad();

    /**
     * Invoked once the game has been closed
     * this is executed at the start of {@link net.minecraft.client.Minecraft#shutdown}
     */
    void onClose();


    /**
     * Invoked on debug call. Can be used to add things into crash reports
     * <p>
     * This does not need to be overriden if it's not needed
     */
    default void sendDebugInfo() {
    }
}
