package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.bipush
import codes.som.anthony.koffee.insns.jvm.invokestatic
import codes.som.anthony.koffee.insns.jvm.sipush
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.IntInsnNode

class RenderTransformer : ConflictTransformer {
    override fun getClassName() = "biv"

    // todo: renderLivingLabel
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "renderName") {
                val list = assembleBlock {
                    sipush(4096)
                    bipush(64)
                    invokestatic(Math::class, "min", int, int, int)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is IntInsnNode) {
                        method.instructions.insertBefore(insn, list)
                        method.instructions.remove(insn)
                    }
                }
            }

            if (method.name == "renderOffsetLivingLabel") {
                val list = assembleBlock {
                    sipush(4096)
                    bipush(64)
                    invokestatic(Math::class, "min", int, int, int)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is IntInsnNode) {
                        method.instructions.insertBefore(insn, list)
                        method.instructions.remove(insn)
                    }
                }
            }
        }

        return original
    }
}