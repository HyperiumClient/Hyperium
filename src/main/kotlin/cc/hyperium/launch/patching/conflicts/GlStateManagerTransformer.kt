package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.GlStateManager
import org.objectweb.asm.tree.ClassNode

class GlStateManagerTransformer : ConflictTransformer {
    override fun getClassName() = "bfl"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public + static, "isTexture2DEnabled", boolean) {
                getstatic(GlStateManager::class, "textureState", "[Lbfl\$r;")
                getstatic(GlStateManager::class, "activeTextureUnit", int)
                aaload
                getfield("bfl\$r", "texture2DState", "bfl\$c;")
                invokestatic("bfl\$c", "access\$1200", boolean, "bfl\$c")
                ireturn
                maxStack = 2
                maxLocals = 0
            }
        }

        return original
    }
}
