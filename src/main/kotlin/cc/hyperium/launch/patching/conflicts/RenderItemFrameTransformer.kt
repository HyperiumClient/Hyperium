package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.getstatic
import codes.som.anthony.koffee.insns.jvm.ifeq
import org.objectweb.asm.tree.ClassNode

class RenderItemFrameTransformer : ConflictTransformer {
    override fun getClassName() = "bjg"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "doRender") {
                val returnMethod = assembleBlock {
                    getstatic(Settings::class, "DISABLE_ITEMFRAMES", boolean)
                    ifeq(L["1"])
                    _return
                    +L["1"]
                }.first

                method.instructions.insertBefore(method.instructions.first, returnMethod)
            }
        }

        return original
    }
}
