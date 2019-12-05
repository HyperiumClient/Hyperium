package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.getstatic
import codes.som.anthony.koffee.insns.jvm.ifeq
import org.objectweb.asm.tree.ClassNode

class RenderItemFrameTransformer : ConflictTransformer {
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "doRender") {
                val instructions = assembleBlock {
                    getstatic(Settings::class, "DISABLE_ITEMFRAMES", boolean)
                    ifeq(L["1"])
                    _return
                    +L["1"]
                }.first
                instructions.add(method.instructions)
                method.instructions = instructions
            }
        }
        return original
    }

    override fun getClassName() = "net.minecraft.client.renderer.tileentity.RenderItemFrame"
}
