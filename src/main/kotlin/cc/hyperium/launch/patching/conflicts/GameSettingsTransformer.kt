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
        original.visitField(Opcodes.ACC_PUBLIC, "allKeys", "Ljava/util/List;", null, null).visitEnd()
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

        original.methods.forEach {
            when (it.name) {
                "<init>" -> {
                    val (createAllKeys) = assembleBlock {
                        aload_0
                        new(ArrayList::class)
                        dup
                        invokespecial(ArrayList::class, "<init>", void)
                        putfield(GameSettings::class, "allKeys", List::class)
                    }

                    if (it.desc == "(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V") {
                        for (insn in it.instructions.iterator()) {

                        }
                    }
                }

                "<clinit>" -> {
                    val (createNewArrayItem) = assembleBlock {
                        dup
                        iconst_3
                        ldc("options.particles.disabled")
                        aastore
                    }

                    for (insn in it.instructions.iterator()) {
                        val (changeNode) = assembleBlock {
                            iconst_4
                        }

                        if (insn is LdcInsnNode && insn.cst == "options.particles.minimal") {
                            it.instructions.insertBefore(insn.next?.next, createNewArrayItem)
                        }

                        val next = insn.next?.next?.next?.next
                        if (insn is InsnNode && insn.opcode == Opcodes.ICONST_3 && next is LdcInsnNode && next.cst == "options.particles.all") {
                            it.instructions.insertBefore(insn, changeNode)
                            it.instructions.remove(insn)
                        }
                    }
                }

                "setOptionFloatValue" -> {
                    val (insertBoolean) = assembleBlock {
                        aload_0
                        iconst_1
                        putfield(GameSettings::class, "needsResourceRefresh", boolean)
                        _return
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is MethodInsnNode && insn.owner == "net/minecraft/client/Minecraft" &&
                            insn.name == "scheduleResourcesRefresh" && insn.desc == "()Lcom/google/common/util/concurrent/ListenableFuture;"
                        ) {
                            it.instructions.insertBefore(insn.previous?.previous, insertBoolean)
                        }
                    }

                    it.instructions.insert(
                        MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            "net/minecraft/client/renderer/texture/TextureMap",
                            "setBlurMipmapDirect",
                            "(ZZ)V",
                            false
                        ), insertBoolean
                    )
                }

                "setOptionValue" -> {
                    val (changeNode) = assembleBlock {
                        iconst_4
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is InsnNode && insn.opcode == Opcodes.ICONST_3 &&
                            insn.next?.next is FieldInsnNode && (insn.next
                                ?.next as FieldInsnNode).name == "particleSetting"
                        ) {
                            it.instructions.insertBefore(insn, changeNode)
                            it.instructions.remove(insn)
                        }
                    }
                }
            }
        }

        return original
    }
}
