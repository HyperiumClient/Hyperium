package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonLoadException
import cc.hyperium.internal.addons.misc.AddonManifestParser
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.FileResourcePack
import net.minecraft.client.resources.FolderResourcePack
import net.minecraft.client.resources.IResourcePack
import net.minecraft.launchwrapper.Launch

import java.io.File
import java.lang.reflect.Field
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

        val uri = file.toURI()
        Launch.classLoader.addURL(uri.toURL())

        val resourcePack = FileResourcePack(file)
        var defaultResourcePacksField: Field
        try {
            defaultResourcePacksField = Minecraft.getMinecraft().javaClass.getDeclaredField("aA")
        } catch (e: NoSuchFieldException) {
            defaultResourcePacksField = Minecraft.getMinecraft().javaClass.getDeclaredField("defaultResourcePacks")
        }

        defaultResourcePacksField.isAccessible = true
        var packs: List<IResourcePack> = defaultResourcePacksField.get(Minecraft.getMinecraft()) as List<IResourcePack>

        packs.plus(resourcePack)

        defaultResourcePacksField.set(Minecraft.getMinecraft(), packs)

        return manifest
    }
}
