package cc.hyperium.launch.patching.conflicts

import cc.hyperium.hooks.ThreadDownloadImageDataHook
import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.invokestatic
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.ThreadDownloadImageData
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ThreadDownloadImageDataTransformer : ConflictTransformer {
    override fun getClassName() = "bma"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.filter {
            it.name == "cacheFile" || it.name == "imageUrl" || it.name == "imageBuffer"
        }.forEach {
            it.access = Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL
        }

        original.methods.find {
            it.name == "loadTextureFromServer"
        }?.apply {
            instructions.clear()
            localVariables.clear()
            tryCatchBlocks.clear()
            koffee {
                aload_0
                invokestatic(
                    ThreadDownloadImageDataHook::class,
                    "loadMultithreadedTexture",
                    void,
                    ThreadDownloadImageData::class
                )
                _return
            }
        }

        return original
    }
}