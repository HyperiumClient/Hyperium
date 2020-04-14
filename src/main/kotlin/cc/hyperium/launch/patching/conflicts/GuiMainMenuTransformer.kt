package cc.hyperium.launch.patching.conflicts

import cc.hyperium.hooks.GuiMainMenuHook
import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.invokestatic
import codes.som.anthony.koffee.koffee
import net.minecraft.client.gui.GuiMainMenu
import org.objectweb.asm.tree.ClassNode

class GuiMainMenuTransformer : ConflictTransformer {
    override fun getClassName() = "aya"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.forEach {
            when (it.name) {
                "initGui" -> {
                    it.instructions.koffee {
                        aload_0
                        invokestatic(GuiMainMenuHook::class, "initGui", void, GuiMainMenu::class)
                        _return
                    }
                }

                "drawScreen" -> {
                    it.instructions.koffee {
                        invokestatic(GuiMainMenuHook::class, "drawScreen", void)
                        _return
                    }
                }
            }
        }
        return original
    }
}
