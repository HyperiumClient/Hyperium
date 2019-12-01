package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.iconst_0
import codes.som.anthony.koffee.insns.jvm.putfield
import codes.som.anthony.koffee.koffee
import net.minecraft.client.model.ModelRenderer
import org.objectweb.asm.tree.ClassNode

class ModelRendererTransformer : ConflictTransformer {
    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "reset", void) {
                aload_0
                iconst_0
                putfield(ModelRenderer::class, "t", boolean)
                _return
                maxStack = 2
                maxLocals = 1
            }
        }
        return original
    }

    override fun getClassName() = "bct"
}
