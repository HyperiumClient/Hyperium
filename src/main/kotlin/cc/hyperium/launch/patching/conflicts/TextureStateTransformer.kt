package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class TextureStateTransformer : ConflictTransformer {
    override fun getClassName() = "bfl\$r"
    override fun transform(original: ClassNode) =
        original.apply { access = Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC }
}
