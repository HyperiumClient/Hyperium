package cc.hyperium.launch.patching.conflicts

import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.RenderEvent
import cc.hyperium.event.render.RenderWorldEvent
import cc.hyperium.hooks.EntityRendererHook
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.profiler.Profiler
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
                            val previous = insn.previous?.previous?.previous
                            if (insn.cst == "outline") {
                                val (createDrawBlockHighlightEvent) = assembleBlock {
                                    aload_0
                                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                                    fload_2
                                    invokestatic(EntityRendererHook::class, "postDrawBlock", void, Minecraft::class, float)
                                }

                                it.instructions.insertBefore(previous, createDrawBlockHighlightEvent)
                            } else if (insn.cst == "hand") {
                                it.instructions.insertBefore(previous, createRenderWorldEvent)
                            }
                        }
                    }
                }
            }
        }

        return original
    }
}
