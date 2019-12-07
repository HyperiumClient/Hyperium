package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class BooleanStateTransformer : ConflictTransformer {
    override fun getClassName() = "bfl\$c"

    override fun transform(original: ClassNode) =
        original.apply { access = Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC }
}
