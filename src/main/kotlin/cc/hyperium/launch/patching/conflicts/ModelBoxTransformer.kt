package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import jdk.internal.org.objectweb.asm.Opcodes
import net.minecraft.client.model.ModelBox
import net.minecraft.client.model.ModelRenderer
import net.minecraft.client.model.PositionTextureVertex
import net.minecraft.client.model.TexturedQuad
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode

private fun MethodAssembly.addQuadListStuff(vertexPositionOpcode: Int, offset: Int, name: String) {
    aload_0
    getfield(ModelBox::class, "quadList", Array<TexturedQuad>::class)
    iload_2
    aaload
    getfield(TexturedQuad::class, "vertexPositions", Array<PositionTextureVertex>::class)
    instructions.add(InsnNode(vertexPositionOpcode))
    aaload
    dup
    getfield(PositionTextureVertex::class, name, float)
    fload(offset)
    aload_1
    getfield(ModelRenderer::class, "textureWidth", float)
    fdiv
    fadd
    putfield(PositionTextureVertex::class, name, float)
}

class ModelBoxTransformer : ConflictTransformer {
    override fun getClassName() = "net.minecraft.client.model.ModelBox"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            node.interfaces.add("cc/hyperium/utils/model/IModelBox")
            method5(public, "offsetTextureQuad", void, ModelRenderer::class, int, float, float) {
                iload_2
                iflt(L["1"])
                iconst_1
                goto(L["2"])

                +L["1"]
                iconst_0

                +L["2"]
                iload_2
                aload_0
                getfield(ModelBox::class, "quadList", Array<TexturedQuad>::class)
                arraylength
                if_icmpge(L["3"])
                iconst_1
                goto(L["4"])

                +L["3"]
                iconst_0

                +L["4"]
                iand
                ifeq(L["5"])

                addQuadListStuff(Opcodes.ICONST_0, 3, "texturePositionX")
                addQuadListStuff(Opcodes.ICONST_1, 3, "texturePositionX")
                addQuadListStuff(Opcodes.ICONST_2, 3, "texturePositionX")
                addQuadListStuff(Opcodes.ICONST_3, 3, "texturePositionX")
                addQuadListStuff(Opcodes.ICONST_0, 4, "texturePositionY")
                addQuadListStuff(Opcodes.ICONST_1, 4, "texturePositionY")
                addQuadListStuff(Opcodes.ICONST_2, 4, "texturePositionY")
                addQuadListStuff(Opcodes.ICONST_3, 4, "texturePositionY")

                +L["5"]
                _return

                maxStack = 4
                maxLocals = 5
            }
        }
        return original
    }
}
