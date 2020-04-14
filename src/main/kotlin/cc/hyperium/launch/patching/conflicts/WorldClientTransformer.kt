package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.IntInsnNode

class WorldClientTransformer : ConflictTransformer {
    override fun getClassName() = "bdb"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.find {
            it.name == "doVoidFogParticles"
        }?.apply {
            for (insn in instructions.iterator()) {
                if (insn is IntInsnNode && insn.operand == 1000) {
                    insn.operand = 500
                    break
                }
            }
        }

        return original
    }
}
