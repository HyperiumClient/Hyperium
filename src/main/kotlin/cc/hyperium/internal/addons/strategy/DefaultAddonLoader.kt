package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonBootstrap
import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonLoadException
import cc.hyperium.internal.addons.misc.AddonManifestParser
import net.minecraft.launchwrapper.Launch

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
    override fun load(file: File?): AddonManifest? {
        if(file == null) {
            throw AddonLoadException("Could not load file; parameter issued was null.")
        }

        val jar = JarFile(file)
        val manifest = AddonManifestParser(jar).getAddonManifest()

        if (jar.getJarEntry("pack.mcmeta") != null) {
            AddonBootstrap.addonResourcePacks.add(file)
        }

        val uri = file.toURI()
        Launch.classLoader.addURL(uri.toURL())

        return manifest
    }
}
