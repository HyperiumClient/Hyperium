package cc.hyperium.resources

import net.minecraft.client.resources.AbstractResourcePack
import java.io.File
import java.io.InputStream

class ClassLoaderResourcePack(domain: String) : AbstractResourcePack(File("ClassLoaderResourcePack:$domain")) {
    override fun getInputStreamByName(name: String?): InputStream? {
        return if (name == null) null else javaClass.getResourceAsStream("/$name")
    }

    override fun hasResourceName(name: String?) = getInputStreamByName(name) != null

    private val domainSet = setOf(domain)

    override fun getResourceDomains() = domainSet
}
