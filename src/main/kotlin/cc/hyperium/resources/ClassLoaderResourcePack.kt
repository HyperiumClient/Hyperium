package cc.hyperium.resources

import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.resources.AbstractResourcePack
import net.minecraft.client.resources.IResourcePack
import net.minecraft.client.resources.data.IMetadataSection
import net.minecraft.client.resources.data.IMetadataSerializer
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.IllegalArgumentException

class ClassLoaderResourcePack(private val domain: String) : IResourcePack {

    private val domainSet = setOf(domain)

    override fun getInputStream(location: ResourceLocation?) =
        getResourceStream(location) ?: throw FileNotFoundException(location?.resourcePath)

    private fun getResourceStream(location: ResourceLocation?): InputStream? {
        val nLocation = location ?: throw IllegalArgumentException("Resource location may not be null")
        return javaClass.getResourceAsStream("/assets/${nLocation.resourceDomain}/${nLocation.resourcePath}")
    }

    override fun resourceExists(location: ResourceLocation?) = getResourceStream(location) != null

    override fun getResourceDomains() = domainSet

    override fun <T : IMetadataSection?> getPackMetadata(
        metadataSerializer: IMetadataSerializer?,
        metadataSectionName: String?
    ): T? {
        return try {
            AbstractResourcePack.readMetadataPublic<T>(
                metadataSerializer,
                javaClass.getResourceAsStream("/pack.mcmeta"),
                metadataSectionName
            )
        } catch (ex: RuntimeException) {
            null
        } catch (ex: FileNotFoundException) {
            null
        }
    }

    override fun getPackImage(): BufferedImage =
        TextureUtil.readBufferedImage(javaClass.getResourceAsStream("/pack.png"))

    override fun getPackName() = "ClassLoaderResourcePack:$domain"
}
