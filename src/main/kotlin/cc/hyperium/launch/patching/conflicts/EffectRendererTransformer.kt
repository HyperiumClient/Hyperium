package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class EffectRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bec"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.find {
            it.name == "particleTypes"
        }?.apply {
            access = Opcodes.ACC_PUBLIC
        }

        return original
    }
}
