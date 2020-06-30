package cc.hyperium.launch.deobf

import cc.hyperium.launch.deobf.mappings.MappingContainer
import org.objectweb.asm.commons.Remapper

/**
 * ASM remapper that deobfuscates Notch mappings to their MCP counterparts
 */
class DeobfuscatingRemapper(val mappings: MappingContainer) : Remapper() {
    override fun mapFieldName(owner: String, name: String, desc: String) = mappings[owner]?.fields?.get(name) ?: name

    override fun map(typeName: String) = mappings[typeName]?.deobfuscatedName ?: typeName

    override fun mapMethodName(owner: String, name: String, desc: String) = mappings[owner]?.methods?.get(name + desc) ?: name

    override fun mapSignature(signature: String?, typeSignature: Boolean) = signature?.takeIf { "!*" !in it }?.let { super.mapSignature(it, typeSignature) }
}