package cc.hyperium.internal.addons

import cc.hyperium.internal.addons.misc.AddonLoadException
import java.util.*

/**
 * Instance created on the classloader {@link net.minecraft.launchwrapper.LaunchClassLoader}
 *
 * @since 1.0
 * @author Kevin Brewster
 */
object AddonMinecraftBootstrap {


    @JvmStatic
    val LOADED_ADDONS = ArrayList<IAddon>()
        @JvmName("getLoadedAddons") get

    /**
     * The <i>init</i> phase of the bootstrap where the
     * instances are created and loaded to {@link me.kbrewster.blazeapi.BlazeAPI#LOADED_ADDONS}
     * and then sets the phase to {@link Phase#DEFAULT}.
     *
     * This should be called when <i>Minecraft</i> is starting
     * In this case we use the start of {@link net.minecraft.client.Minecraft#init}
     */
    @JvmStatic
    fun init() {
        if (AddonBootstrap.phase != AddonBootstrap.Phase.INIT) {
            throw AddonLoadException("Bootstrap is currently at Phase.${AddonBootstrap.phase} when it should be at Phase.INIT")
        }

        val loaded = AddonBootstrap.addonManifests
                .sortedWith(DependencyComparator())
                .map { Class.forName(it.mainClass).newInstance() }
                .filter { it is IAddon }
                .map { it as IAddon }
                .toCollection(ArrayList())

        LOADED_ADDONS.addAll(loaded)
        LOADED_ADDONS.forEach(IAddon::onLoad)
        AddonBootstrap.phase = AddonBootstrap.Phase.DEFAULT
    }

}