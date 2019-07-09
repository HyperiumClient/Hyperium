package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonBootstrap
import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonManifestParser
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset

/**
 * Used to load the addon if the addon is
 * being made inside of the dev environment
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class WorkspaceAddonLoader : AddonLoaderStrategy() {

    /**
     * Does not need to be added to the classloader as
     * it gets compiled from the dev environment
     */
    @Throws(Exception::class)
    override fun load(file: File?): AddonManifest? {
        val resource = javaClass.classLoader.getResource("addon.json") ?: return null // not in workspace
        if (javaClass.classLoader.getResource("pack.mcmeta") != null) {
            AddonBootstrap.addonResourcePacks.add(file)
        }

        val lines = IOUtils.toString(resource.openStream(), Charset.defaultCharset())

        val parser = AddonManifestParser(lines)

        return parser.getAddonManifest()
    }
}
