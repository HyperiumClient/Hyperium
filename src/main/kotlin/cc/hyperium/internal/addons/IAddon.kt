package cc.hyperium.internal.addons

/**
 * Interface of which the main class
 * of an Addon must implement
 *
 * @since 1.0
 * @author Kevin Brewster
 */
interface IAddon {

    /**
     * Invoked once the plugin has successfully loaded
     * {@see cc.hyperium.internal.addons.AddonMinecraftBootstrap#init}
     */
    fun onLoad()

    /**
     * Invoked once the game has been closed
     * this is executed at the start of {@link net.minecraft.client.Minecraft#shutdown}
     */
    fun onClose()


    /**
     * Invoked on debug call
     */
    fun sendDebugInfo()

}
