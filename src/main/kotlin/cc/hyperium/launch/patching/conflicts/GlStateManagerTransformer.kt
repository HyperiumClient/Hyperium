package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class GlStateManagerTransformer : ConflictTransformer {
    override fun getClassName() = "bfl"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.filter {
            it.name in publicFields
        }.forEach { it.access = Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC }
        return original
    }

    companion object {
        private val publicFields = arrayOf("activeTextureUnit", "textureState", "colorState")
    }

    class GlStateSubclassTransformer(private val name: String) : ConflictTransformer {
        override fun getClassName() = "bfl\$$name"

        override fun transform(original: ClassNode): ClassNode {
            original.access = Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC
            return original
        }

    }
}