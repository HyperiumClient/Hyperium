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
        original.methods.forEach {
            when (it.name) {
                "renderName",
                "renderOffsetLivingLabel" -> {
                    val (list) = assembleBlock {
                        sipush(4096)
                        bipush(64)
                        invokestatic(Math::class, "min", int, int, int)
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is IntInsnNode) {
                            it.instructions.insertBefore(insn, list)
                            it.instructions.remove(insn)
                        }
                    }
                }
            }
        }

        return original
    }
}