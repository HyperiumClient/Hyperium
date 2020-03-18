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
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
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
                    val (initializeShaderHelper) = assembleBlock {
                        new(ShaderHelper::class)
                        dup
                        aload_0
                        invokespecial(ShaderHelper::class, "<init>", void, EntityRenderer::class)
                        pop
                    }

                    it.instructions.insertBefore(it.instructions.last.previous, initializeShaderHelper)
                }

                "loadShader" -> {
                    it.access = Opcodes.ACC_PUBLIC
                }

                "updateCameraAndRender" -> {
                    val (modifyRotation) = assembleBlock {
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        invokestatic(EntityRendererHook::class, "updateRendererHook", void, Minecraft::class)
                    }

                    val (postRenderEvent) = assembleBlock {
                        getstatic(EventBus::class, "INSTANCE", EventBus::class)
                        new(RenderEvent::class)
                        dup
                        invokespecial(RenderEvent::class, "<init>", void)
                        invokevirtual(EventBus::class, "post", void, Event::class)
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is LdcInsnNode) {
                            if (insn.cst == "mouse") {
                                it.instructions.insertBefore(insn.previous?.previous?.previous, modifyRotation)
                            }

                            if (insn.cst == "gui") {
                                it.instructions.insertBefore(insn.next, postRenderEvent)
                            }
                        }
                    }
                }

                "renderWorldPass" -> {
                    val (postDrawBlockHighlightEvent) = assembleBlock {
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
                        invokespecial(
                            DrawBlockHighlightEvent::class,
                            "<init>",
                            void,
                            EntityPlayer::class,
                            MovingObjectPosition::class,
                            float
                        )
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

                    val (postRenderWorldEvent) = assembleBlock {
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "mcProfiler", Profiler::class)
                        ldc("hyperium_render_last")
                        invokevirtual(Profiler::class, "startSection", void, String::class)
                        new(RenderWorldEvent::class)
                        dup
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "renderGlobal", RenderGlobal::class)
                        fload_2
                        invokespecial(RenderWorldEvent::class, "<init>", void, RenderGlobal::class, float)
                        invokevirtual(RenderWorldEvent::class, "post", void)
                        aload_0
                        getfield(EntityRenderer::class, "mc", Minecraft::class)
                        getfield(Minecraft::class, "mcProfiler", Profiler::class)
                        invokevirtual(Profiler::class, "endSection", void)
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is LdcInsnNode) {
                            if (insn.cst == "outline") {
                                it.instructions.insertBefore(
                                    insn.previous?.previous?.previous,
                                    postDrawBlockHighlightEvent
                                )
                                // two of them, so don't break
                            }

                            if (insn.cst == "hand") {
                                it.instructions.insertBefore(insn.previous?.previous?.previous, postRenderWorldEvent)
                            }
                        }
                    }
                }

                // todo
                "orientCamera" -> {
                    it.instructions = assembleBlock {
                        aload_0
                        fload_1
                        invokestatic(EntityRendererHook::class, "orientCameraHook", void, EntityRenderer::class, float)
                        _return
                    }.first
                }
            }
        }

        return original
    }
}
