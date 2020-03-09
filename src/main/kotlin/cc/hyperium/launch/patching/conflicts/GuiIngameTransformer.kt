package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.config.Settings
import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.RenderHUDEvent
import cc.hyperium.event.render.RenderSelectedItemEvent
import cc.hyperium.gui.ScoreboardRenderer
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.profiler.Profiler
import net.minecraft.scoreboard.ScoreObjective
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class GuiIngameTransformer : ConflictTransformer {
    override fun getClassName() = "avo"

    // todo renderBossHealth & renderPlayerStats
    override fun transform(original: ClassNode): ClassNode {
        original.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "renderScoreboard",
            "Z",
            null,
            true
        ).visitEnd()
        original.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "renderHealth",
            "Z",
            null,
            true
        ).visitEnd()
        original.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "renderFood",
            "Z",
            null,
            true
        ).visitEnd()
        original.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            "renderArmor",
            "Z",
            null,
            true
        ).visitEnd()

        for (method in original.methods) {
            if (method.name == "renderGameOverlay") {
                val profileHyperiumOverlay = assembleBlock {
                    aload_0
                    getfield(GuiIngame::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "mcProfiler", Profiler::class)
                    ldc("hyperium_overlay")
                    invokevirtual(Profiler::class, "startSection", void, String::class)
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    new(RenderHUDEvent::class)
                    dup
                    new(ScaledResolution::class)
                    dup
                    aload_0
                    getfield(GuiIngame::class, "mc", Minecraft::class)
                    invokespecial(ScaledResolution::class, "<init>", void, Minecraft::class)
                    fload_1
                    invokespecial(RenderHUDEvent::class, "<init>", void, ScaledResolution::class, float)
                    invokevirtual(EventBus::class, "post", void, Event::class)
                    fconst_1
                    fconst_1
                    fconst_1
                    fconst_1
                    invokestatic(GlStateManager::class, "color", void, float, float, float, float)
                    aload_0
                    getfield(GuiIngame::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "mcProfiler", Profiler::class)
                    invokevirtual(Profiler::class, "endSection", void)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is MethodInsnNode && insn.name == "getObjectiveInDisplaySlot" && insn.previous?.opcode == Opcodes.ICONST_0) {
                        method.instructions.insertBefore(insn.next?.next, profileHyperiumOverlay)
                    }
                }
            }

            if (method.name == "renderSelectedItem") {
                val postRenderSelectedItemEvent = assembleBlock {
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    new(RenderSelectedItemEvent::class)
                    dup
                    aload_1
                    invokespecial(RenderSelectedItemEvent::class, "<init>", void, ScaledResolution::class)
                    invokevirtual(EventBus::class, "post", void, Event::class)
                    fconst_1
                    fconst_1
                    fconst_1
                    fconst_1
                    invokestatic(GlStateManager::class, "color", void, float, float, float, float)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is MethodInsnNode && insn.name == "disableBlend") {
                        method.instructions.insertBefore(insn.next, postRenderSelectedItemEvent)
                    }
                }
            }

            if (method.name == "renderScoreboard") {
                method.instructions.clear()
                method.localVariables.clear()
                method.instructions.koffee {
                    getstatic(GuiIngame::class, "renderScoreboard", boolean)
                    ifne(L["1"])
                    _return
                    +L["1"]
                    aload_1
                    putstatic(ScoreboardDisplay::class, "objective", ScoreObjective::class)
                    aload_2
                    putstatic(ScoreboardDisplay::class, "resolution", ScaledResolution::class)
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getScoreboardRenderer", ScoreboardRenderer::class)
                    aload_1
                    aload_2
                    invokevirtual(ScoreboardRenderer::class, "render", void, ScoreObjective::class, ScaledResolution::class)
                    _return
                }
            }

            if (method.name == "displayTitle") {
                val disableTitles = assembleBlock {
                    getstatic(Settings::class, "HIDE_TITLES", boolean)
                    ifeq(L["1"])
                    _return
                    +L["1"]
                }.first

                method.instructions.insertBefore(method.instructions.first, disableTitles)
            }
        }

        return original
    }
}