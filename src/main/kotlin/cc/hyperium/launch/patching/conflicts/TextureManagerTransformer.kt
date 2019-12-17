package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.areturn
import codes.som.anthony.koffee.insns.jvm.getfield
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.texture.TextureManager
import org.objectweb.asm.tree.ClassNode

class TextureManagerTransformer : ConflictTransformer {
    override fun getClassName() = "bmj"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "getMapTextureObjects", Map::class) {
                aload_0
                getfield(TextureManager::class, "mapTextureObjects", Map::class)
                areturn
                maxStack = 1
                maxLocals = 1
            }
        }

        return original
    }

}
