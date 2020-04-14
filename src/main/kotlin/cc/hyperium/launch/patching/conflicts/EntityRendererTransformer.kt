package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.DrawBlockHighlightEvent
import cc.hyperium.event.render.RenderEvent
import cc.hyperium.event.render.RenderWorldEvent
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.handlers.handlers.OtherConfigOptions
import cc.hyperium.hooks.EntityRendererHook
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.profiler.Profiler
import net.minecraft.util.MovingObjectPosition
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode

class EntityRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bfk"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.filter {
            it.name == "renderHand" || it.name == "thirdPersonDistance" || it.name == "thirdPersonDistanceTemp" || it.name == "cloudFog"
        }.forEach {
            it.access = Opcodes.ACC_PUBLIC
        }

        original.methods.forEach {
            when (it.name) {
                "<init>" -> {
                    val (createShaderHelper) = assembleBlock {
                        new(ShaderHelper::class)
                        dup
                        aload_0
                        invokespecial(ShaderHelper::class, "<init>", void, EntityRenderer::class)
                        pop
                    }

                    it.instructions.insertBefore(it.instructions.last.previous, createShaderHelper)
                }

                "loadShader" -> it.access = Opcodes.ACC_PUBLIC

                "orientCamera" -> {
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.tryCatchBlocks.clear()
                    it.koffee {
                        aload_0
                        fload_1
                        invokestatic(EntityRendererHook::class, "orientCameraHook", void, EntityRenderer::class, float)
                        _return
                    }
                }

                "updateCameraAndRender" -> {
                    val (updateRendererHook) = assembleBlock {
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        invokestatic(EntityRendererHook::class, "updateRendererHook", void, Minecraft::class)
                    }

                    val (renderEventPost) = assembleBlock {
                        getstatic(EventBus::class, "INSTANCE", EventBus::class)
                        new(RenderEvent::class)
                        dup
                        invokespecial(RenderEvent::class, "<init>", void)
                        invokevirtual(EventBus::class, "post", void, Event::class)
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is LdcInsnNode) {
                            if (insn.cst == "mouse") {
                                it.instructions.insertBefore(insn.previous?.previous?.previous, updateRendererHook)
                            } else if (insn.cst == "gui") {
                                it.instructions.insertBefore(insn.next?.next, renderEventPost)
                            }
                        }
                    }
                }

                "renderWorldPass" -> {
                    val (createDrawBlockHighlightEvent) = assembleBlock {
                        new(DrawBlockHighlightEvent::class)
                        dup
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        invokevirtual(Minecraft::class, "getRenderViewEntity", Entity::class)
                        checkcast(EntityPlayer::class)
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "objectMouseOver", MovingObjectPosition::class)
                        fload_2
                        invokespecial(DrawBlockHighlightEvent::class, "<init>", void, EntityPlayer::class, MovingObjectPosition::class, float)
                        astore(17)
                        getstatic(EventBus::class, "INSTANCE", EventBus::class)
                        aload(17)
                        invokevirtual(EventBus::class, "post", void, Event::class)
                        aload(17)
                        invokevirtual(DrawBlockHighlightEvent::class, "isCancelled", boolean)
                        ifeq(L["78"])
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                        invokevirtual(HyperiumHandlers::class, "getConfigOptions", OtherConfigOptions::class)
                        iconst_1
                        putfield(OtherConfigOptions::class, "isCancelBox", boolean)
                        +L["78"]
                    }

                    val (createRenderWorldEvent) = assembleBlock {
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "mcProfiler", Profiler::class)
                        ldc("hyperium_render_last")
                        invokevirtual(Profiler::class, "startSection", void, String::class)
                        getstatic(EventBus::class, "INSTANCE", EventBus::class)
                        new(RenderWorldEvent::class)
                        dup
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "renderGlobal", RenderGlobal::class)
                        fload_2
                        invokespecial(RenderWorldEvent::class, "<init>", void, RenderGlobal::class, float)
                        invokevirtual(EventBus::class, "post", void, Event::class)
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "mcProfiler", Profiler::class)
                        invokevirtual(Profiler::class, "endSection", void)
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is LdcInsnNode) {
                            if (insn.cst == "outline") {
                                it.instructions.insertBefore(insn.previous?.previous?.previous, createDrawBlockHighlightEvent)
                            }

                            if (insn.cst == "hand") {
                                it.instructions.insertBefore(insn.previous?.previous?.previous, createRenderWorldEvent)
                            }
                        }
                    }
                }
            }
        }

        return original
    }
}
