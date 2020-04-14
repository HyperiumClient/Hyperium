package cc.hyperium.launch.patching.conflicts

import cc.hyperium.hooks.ScreenShotHelperHook
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.IChatComponent
import net.minecraft.util.ScreenShotHelper
import org.objectweb.asm.tree.ClassNode
import java.nio.IntBuffer

class ScreenShotHelperTransformer : ConflictTransformer {
    override fun getClassName() = "avj"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.find {
            it.name == "saveScreenshot" && it.desc == "(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;"
        }?.apply {
            instructions.clear()
            localVariables.clear()
            tryCatchBlocks.clear()
            koffee {
                iload_2
                iload_3
                aload(4)
                getstatic(ScreenShotHelper::class, "pixelBuffer", IntBuffer::class)
                getstatic(ScreenShotHelper::class, "pixelValues", Array<Int>::class)
                invokestatic(
                    ScreenShotHelperHook::class,
                    "saveScreenshot",
                    IChatComponent::class,
                    int,
                    int,
                    Framebuffer::class,
                    IntBuffer::class,
                    Array<Int>::class
                )
                areturn
            }
        }

        return original
    }
}