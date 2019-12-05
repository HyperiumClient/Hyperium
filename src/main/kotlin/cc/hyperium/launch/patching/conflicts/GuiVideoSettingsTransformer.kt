package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiVideoSettings
import net.minecraft.client.settings.GameSettings
import org.objectweb.asm.tree.ClassNode

class GuiVideoSettingsTransformer : ConflictTransformer {
    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "m", void) {
                aload_0
                invokespecial(GuiScreen::class, "onGuiClosed", void)

                aload_0
                getfield(GuiVideoSettings::class, "mc", Minecraft::class)
                getfield(Minecraft::class, "gameSettings", GameSettings::class)
                invokevirtual(GameSettings::class, "onGuiClosed", void)

                _return
            }
        }
        return original
    }

    override fun getClassName() = "net.minecraft.client.gui.GuiVideoSettings"
}
