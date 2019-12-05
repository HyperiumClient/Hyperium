package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.aload_1
import codes.som.anthony.koffee.insns.jvm.invokevirtual
import codes.som.anthony.koffee.insns.jvm.ldc
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.VarInsnNode
import java.net.HttpURLConnection

class ThreadDownloadImageDataTransformer : ConflictTransformer {
    override fun getClassName() = "net.minecraft.client.renderer.ThreadDownloadImageData"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "run") {
                val list = assembleBlock {
                    aload_1
                    ldc("User-Agent")
                    ldc("Hyperium Client")
                    invokevirtual(HttpURLConnection::class, "setRequestProperty", void, String::class, String::class)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is VarInsnNode && insn.opcode == Opcodes.ALOAD && insn.`var` == 1) {
                        method.instructions.insertBefore(insn, list)
                        break
                    }
                }
            }
        }
        return original
    }
}
