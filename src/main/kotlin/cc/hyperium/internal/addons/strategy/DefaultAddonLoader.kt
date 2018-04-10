package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonManifestParser
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy

import java.io.File
import java.util.jar.JarFile

/**
 * Used to load by a file into the classloader
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class DefaultAddonLoader : AddonLoaderStrategy() {

    /**
     * Loads the [file] into the classloader
     *
     * @param file addonManifests jar
     * @throws Exception when exception occurs
     */
    @Throws(Exception::class)
    override fun load(file: File): AddonManifest {
        val jar = JarFile(file)
        val manifest = AddonManifestParser(jar).getAddonManifest()

        val uri = file.toURI()
        classloader.addURL(uri.toURL())

        return manifest
    }
}
