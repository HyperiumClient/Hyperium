package cc.hyperium.launch.patching.conflicts

import cc.hyperium.hooks.ThreadDownloadImageDataHook
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.invokestatic
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.ThreadDownloadImageData
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ThreadDownloadImageDataTransformer : ConflictTransformer {
    override fun getClassName() = "bma"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.find {
            it.name == "cacheFile" || it.name == "imageBuffer" || it.name == "imageUrl"
        }?.apply {
            access = Opcodes.ACC_PUBLIC
        }

        original.methods.find {
            it.name == "loadTextureFromServer"
        }?.apply {
            instructions.clear()
            tryCatchBlocks.clear()
            localVariables.clear()
            instructions.koffee {
                aload_0
                invokestatic(
                    ThreadDownloadImageDataHook::class,
                    "loadMultithreadedTexture",
                    void,
                    ThreadDownloadImageData::class
                )
            }
        }

        return original
    }
}