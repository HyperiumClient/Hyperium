package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.iconst_0
import codes.som.anthony.koffee.insns.jvm.putfield
import codes.som.anthony.koffee.koffee
import net.minecraft.client.model.ModelRenderer
import org.objectweb.asm.tree.ClassNode

class ModelRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bct"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            node.interfaces.add("cc/hyperium/utils/model/IModelRenderer")

            method5(public, "reset", void) {
                aload_0
                iconst_0
                putfield(ModelRenderer::class, "compiled", boolean)
                _return
            }
        }

        return original
    }
}
