package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode

class EntityRendererTransformer : ConflictTransformer {
    override fun transform(original: ClassNode): ClassNode {
        // TODO: Finish this
        for (method in original.methods) {
            if (method.name == "<init>") {
                val list = assembleBlock {
                    new(ShaderHelper::class)
                    dup
                    aload_0
                    invokespecial(ShaderHelper::class, "<init>", void, EntityRenderer::class)
                    pop
                }.first

                method.instructions.iterator().forEach {
                    if (it.opcode == Opcodes.RETURN) {
                        method.instructions.insertBefore(it, list)
                    }
                }
            } else if (method.name == "loadShader" && method.desc == "(Lnet/minecraft/util/ResourceLocation;)V") {
                method.access = Opcodes.ACC_PUBLIC
            } else if (method.name == "updateRenderer" && method.desc == "()V") {
                val list = assembleBlock {
                    fconst_1
                    invokevirtual(Entity::class, "getPositionEyes", Vec3::class, float)
                }.first

                for (insn in method.instructions.iterator()) {
                    // new BlockPos(Vec3)
                    if (insn.opcode == Opcodes.INVOKESPECIAL && insn is MethodInsnNode && insn.owner == "net/minecraft/util/BlockPos" && insn.name == "<init>" && insn.desc == "(Lnet/minecraft/util/Vec3;)V") {
                        method.instructions.insertBefore(insn, list)
                        break
                    }
                }
            } else if (method.name == "orientCamera" && method.desc == "(F)V") {
                val list = assembleBlock {
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getPerspectiveHandler", PerspectiveModifierHandler::class)
                    astore(method.maxLocals)
                }.first
                val list2 = assembleBlock {
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["29"])
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fstore(12)
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fstore(13)
                    +L["29"]
                }.first
                val list3 = assembleBlock {
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["53"])

                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fload(13)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fload(12)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)

                    fload(12)
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fload(13)
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)
                    goto(L["26"])

                    +L["53"]
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fload(13)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fload(12)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)

                    fload(12)
                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fload(13)
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)
                    +L["26"]
                }.first

                val list4 = assembleBlock {
                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["71"])

                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload(method.maxLocals)
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    ldc(180f)
                    fadd
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)
                    goto(L["63"])

                    +L["71"]
                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fload(12)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fload(11)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    +L["63"]
                }.first
                list.add(method.instructions)

                method.maxLocals++
                method.instructions = list
                var added2 = false
                var added3 = false
                var added4 = false
                method.instructions.iterator().forEach {
                    if (!added2 && it is VarInsnNode && it.opcode == Opcodes.ALOAD && it.`var` == 0) {
                        var next = it.next
                        if (next is FieldInsnNode && next.opcode == Opcodes.GETFIELD && next.owner == "net/minecraft/client/renderer/EntityRenderer" && next.name == "mc") {
                            next = next.next
                            if (next is FieldInsnNode && next.opcode == Opcodes.GETFIELD && next.owner == "net/minecraft/client/Minecraft" && next.name == "gameSettings") {
                                next = next.next
                                if (next is FieldInsnNode && next.opcode == Opcodes.GETFIELD && next.owner == "net/minecraft/client/settings/GameSettings" && next.name == "thirdPersonView") {
                                    next = next.next
                                    if (next.opcode == Opcodes.ICONST_2) {
                                        next = next.next
                                        if (next.opcode == Opcodes.IF_ICMPNE) {
                                            method.instructions.insertBefore(it, list2)
                                            added2 = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return original
    }

    override fun getClassName() = "net.minecraft.client.renderer.EntityRenderer"
}
