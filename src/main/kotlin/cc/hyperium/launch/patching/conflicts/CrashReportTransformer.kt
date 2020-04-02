package cc.hyperium.launch.patching.conflicts

import cc.hyperium.hooks.CrashReportHook
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.invokestatic
import net.minecraft.crash.CrashReport
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class CrashReportTransformer : ConflictTransformer {
    override fun getClassName() = "b"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.find {
            it.name == "theReportCategory"
        }?.apply {
            access = Opcodes.ACC_PUBLIC
        }

        original.methods.find {
            it.name == "populateEnvironment"
        }?.apply {
            val (populateHyperium) = assembleBlock {
                aload_0
                invokestatic(CrashReportHook::class, "populateEnvironment", void, CrashReport::class)
            }

            instructions.insertBefore(instructions.first, populateHyperium)
        }

        return original
    }
}
