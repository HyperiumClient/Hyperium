package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import com.google.common.util.concurrent.ListenableFuture
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.GameSettings
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

class GameSettingsTransformer : ConflictTransformer {
    override fun getClassName() = "avh"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(Opcodes.ACC_PUBLIC, "needsResourceRefresh", "Z", null, null).visitEnd()
        original.koffee {
            method5(public, "onGuiClosed", void) {
                aload_0
                getfield(GameSettings::class, "needsResourceRefresh", boolean)
                ifeq(L["1"])
                aload_0
                getfield(GameSettings::class, "mc", Minecraft::class)
                invokevirtual(Minecraft::class, "scheduleResourcesRefresh", ListenableFuture::class)
                pop
                aload_0
                iconst_0
                putfield(GameSettings::class, "needsResourceRefresh", boolean)
                +L["1"]
                _return
                maxStack = 2
                maxLocals = 1
            }
        }

        for (method in original.methods) {
            if (method.name == "<clinit>") {
                val createNewArrayItem = assembleBlock {
                    dup
                    iconst_3
                    ldc("options.particles.disabled")
                    aastore
                }.first

                for (insn in method.instructions.iterator()) {
                    val changeNode = assembleBlock {
                        iconst_4
                    }.first

                    if (insn is LdcInsnNode && insn.cst == "options.particles.minimal") {
                        method.instructions.insertBefore(insn.next?.next, createNewArrayItem)
                    }

                    val next = insn.next?.next?.next?.next
                    if (insn is InsnNode && insn.opcode == Opcodes.ICONST_3 && next is LdcInsnNode && next.cst == "options.particles.all") {
                        method.instructions.insertBefore(insn, changeNode)
                        method.instructions.remove(insn)
                    }
                }
            }

            if (method.name == "setOptionFloatValue") {
                val insertBoolean = assembleBlock {
                    aload_0
                    iconst_1
                    putfield(GameSettings::class, "needsResourceRefresh", boolean)
                    _return
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is MethodInsnNode && insn.owner == "net/minecraft/client/Minecraft" &&
                            insn.name == "scheduleResourcesRefresh" && insn.desc == "()Lcom/google/common/util/concurrent/ListenableFuture;"
                    ) {
                        method.instructions.insertBefore(insn.previous?.previous, insertBoolean)
                    }
                }

                method.instructions.insert(
                        MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "net/minecraft/client/renderer/texture/TextureMap",
                                "setBlurMipmapDirect",
                                "(ZZ)V",
                                false
                        ), insertBoolean
                )
            }

            if (method.name == "setOptionValue") {
                val changeNode = assembleBlock {
                    iconst_4
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is InsnNode && insn.opcode == Opcodes.ICONST_3 &&
                            insn.next?.next is FieldInsnNode && (insn.next?.next as FieldInsnNode).name == "particleSetting"
                    ) {
                        method.instructions.insertBefore(insn, changeNode)
                        method.instructions.remove(insn)
                    }
                }
            }
        }

        return original
    }
}
