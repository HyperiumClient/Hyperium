package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.DrawBlockHighlightEvent
import cc.hyperium.event.render.RenderEvent
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.handlers.handlers.OtherConfigOptions
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

class EntityRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bfk"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "loadShader") {
                method.access = Opcodes.ACC_PUBLIC
            }

            if (method.name == "<init>") {
                val createShaderHelper = assembleBlock {
                    new(ShaderHelper::class)
                    dup
                    aload_0
                    invokespecial(ShaderHelper::class, "<init>", void, EntityRenderer::class)
                    pop
                }.first


                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.RETURN) {
                        method.instructions.insertBefore(insn, createShaderHelper)
                    }
                }
            }

            if (method.name == "updateRenderer") {
                val setPositionEyes = assembleBlock {
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    invokevirtual(Minecraft::class, "getRenderViewEntity", Entity::class)
                    fconst_1
                    invokevirtual(Entity::class, "getPositionEyes", Vec3::class, float)
                    invokespecial(BlockPos::class, "<init>", void, Vec3::class)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.INVOKESPECIAL && insn is MethodInsnNode && insn.owner == "net/minecraft/util/BlockPos"
                            && insn.name == "<init>" && insn.desc == "(DDD)V"
                    ) {
                        method.instructions.insertBefore(insn.previous.previous.previous, setPositionEyes)
                        method.instructions.remove(insn.previous.previous.previous)
                        method.instructions.remove(insn.previous.previous)
                        method.instructions.remove(insn.previous)
                        method.instructions.remove(insn)
                        break
                    }
                }
            }

            if (method.name == "updateCameraAndRender") {
                val newRenderEvent = assembleBlock {
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    new(RenderEvent::class)
                    dup
                    invokespecial(RenderEvent::class, "<init>", void)
                    invokevirtual(EventBus::class, "post", void, Event::class)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is LdcInsnNode && insn.cst == "gui") {
                        method.instructions.insert(insn.next, newRenderEvent)
                    }
                }
            }

            // todo: fix !!
            if (method.name == "renderWorldPass") {
                val drawBlockHighlightEvent = assembleBlock {
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
                    invokespecial(DrawBlockHighlightEvent::class, "<init>", void, EntityPlayer::class, MovingObjectPosition::class)
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
                }.first

                for (insn in method.instructions.iterator()) {
                    val previous = insn.previous?.previous?.previous?.previous
                    if (insn is LdcInsnNode && insn.cst == "outline" &&
                            previous is MethodInsnNode && previous.name != "disableAlpha") {
                        method.instructions.insertBefore(insn.previous?.previous?.previous, drawBlockHighlightEvent)
                    }
                }
            }

            // todo: orientCamera
        }

        return original
    }
}
