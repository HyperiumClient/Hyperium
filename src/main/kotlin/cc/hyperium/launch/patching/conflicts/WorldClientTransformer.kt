package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.IntInsnNode

class WorldClientTransformer : ConflictTransformer {
    override fun getClassName() = "bdb"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "doVoidFogParticles") {
                for (insn in method.instructions.iterator()) {
                    if (insn is IntInsnNode && insn.opcode == Opcodes.SIPUSH && insn.operand == 1000) {
                        insn.operand = 500
                    }
                }
            }
        }

        return original
    }
}
