package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.EntityRenderEvent
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class RendererLivingEntityTransformer : ConflictTransformer {
    override fun getClassName() = "bjl"

    // todo: f2 = f1 - f in doRender;
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "addLayer") {
                method.access = Opcodes.ACC_PUBLIC
            }

            if (method.name == "doRender") {
                val disableArmorstands = assembleBlock {
                    getstatic(Settings::class, "DISABLE_ARMORSTANDS", boolean)
                    ifeq(L["3"])
                    aload_1
                    instanceof(EntityArmorStand::class)
                    ifeq(L["3"])
                    _return
                    +L["3"]
                    new(EntityRenderEvent::class)
                    dup
                    aload_1
                    dload_2
                    d2f
                    dload(4)
                    d2f
                    dload(6)
                    d2f
                    aload_1
                    getfield(Entity::class, "rotationPitch", float)
                    fload(8)
                    fconst_1
                    invokespecial(
                        EntityRenderEvent::class,
                        "<init>",
                        void,
                        Entity::class,
                        float,
                        float,
                        float,
                        float,
                        float,
                        float
                    )
                    astore(10)
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    aload(10)
                    invokevirtual(EventBus::class, "post", void, Event::class)
                    aload(10)
                    invokevirtual(EntityRenderEvent::class, "isCancelled", boolean)
                    ifeq(L["4"])
                    _return
                    +L["4"]
                }.first

                method.instructions.insertBefore(method.instructions.first, disableArmorstands)

                // pain
                var f = 0
                var f1 = 0
                var f2 = 0
                var i = 0

                for (insn in method.instructions.iterator()) {
                    var next = insn

                    if (next is TypeInsnNode) {
                        if (next.opcode == Opcodes.INSTANCEOF && next.desc == "net/minecraft/entity/EntityLivingBase") {
                            while (next.previous?.also { next = it } != null) {
                                if (next is VarInsnNode && next.opcode == Opcodes.FSTORE) {
                                    when (i) {
                                        0 -> {
                                            f2 = (next as VarInsnNode).`var`
                                        }
                                        1 -> {
                                            f1 = (next as VarInsnNode).`var`
                                        }
                                        else -> {
                                            f = (next as VarInsnNode).`var`
                                        }
                                    }

                                    i++
                                    if (i == 3) break
                                }
                            }

                            if (next == null) break

                            var node: LabelNode? = null
                            while (next.next?.also { next = it } != null) {
                                if (next is JumpInsnNode && next.opcode == Opcodes.IFEQ) {
                                    node = (next as JumpInsnNode).label
                                    break
                                }
                            }

                            if (next == null) break

                            val labelNode = LabelNode()
                            while (next.next?.also { next = it } != null) {
                                if (next is JumpInsnNode && (next as JumpInsnNode).label == node && next.opcode == Opcodes.IFLE) {
                                    (next as JumpInsnNode).label = labelNode
                                    break
                                }
                            }

                            if (next == null) break

                            while (next.next?.also { next = it } != null) {
                                if (next == node) {
                                    val list = InsnList()
                                    list.add(labelNode)
                                    list.add(VarInsnNode(Opcodes.FLOAD, f1))
                                    list.add(VarInsnNode(Opcodes.FLOAD, f))
                                    list.add(InsnNode(Opcodes.FSUB))
                                    list.add(VarInsnNode(Opcodes.FSTORE, f2))
                                    method.instructions.insertBefore(next, list)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }

        return original
    }
}