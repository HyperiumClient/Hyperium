package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.utils.DebugOverlayUtil
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiOverlayDebug
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.profiler.Profiler
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class GuiOverlayDebugTransformer : ConflictTransformer {
    override fun getClassName() = "avv"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.find {
            it.name == "renderDebugInfo"
        }?.apply {
            val (renderOldOverlay) = assembleBlock {
                getstatic(Settings::class, "OLD_DEBUG", boolean)
                ifeq(L["3"])
                getstatic(DebugOverlayUtil::class, "INSTANCE", DebugOverlayUtil::class)
                invokevirtual(DebugOverlayUtil::class, "renderOldDebugInfoLeft", void)
                getstatic(DebugOverlayUtil::class, "INSTANCE", DebugOverlayUtil::class)
                aload_1
                invokevirtual(DebugOverlayUtil::class, "renderOldDebugInfoRight", void, ScaledResolution::class)
                invokestatic(GlStateManager::class, "popMatrix", void)
                aload_0
                getfield(GuiOverlayDebug::class, "mc", Minecraft::class)
                getfield(Minecraft::class, "mcProfiler", Profiler::class)
                invokevirtual(Profiler::class, "endSection", void)
                _return
                +L["3"]
            }

            for (insn in instructions.iterator()) {
                if (insn is MethodInsnNode && insn.name == "renderDebugInfoLeft") {
                    instructions.insertBefore(insn.previous, renderOldOverlay)
                }
            }
        }

        return original
    }
}