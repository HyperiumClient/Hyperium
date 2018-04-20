package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonManifestParser
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.FolderResourcePack
import net.minecraft.client.resources.IResourcePack
import org.apache.commons.io.IOUtils
import java.io.File
import java.lang.reflect.Field
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
        val resourcePack = FolderResourcePack(file)
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

        val lines = IOUtils.toString(resource.openStream(), Charset.defaultCharset())

        val parser = AddonManifestParser(lines)

        return parser.getAddonManifest()
    }
}
