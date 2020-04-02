package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.texture.ITextureObject
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.ResourceLocation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

class TextureManagerTransformer : ConflictTransformer {
    override fun getClassName() = "bmj"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.find { it.name == "mapTextureObjects"
        }?.apply {
            access = Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL
        }

        original.visitField(
            Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
            "textures",
            "Ljava/util/HashMap;",
            null,
            null
        ).visitEnd()

        original.methods.forEach {
            when (it.name) {
                "<init>" -> {
                    val (createTextures) = assembleBlock {
                        aload_0
                        new(HashMap::class)
                        dup
                        invokespecial(HashMap::class, "<init>", void)
                        putfield(TextureManager::class, "textures", HashMap::class)
                    }

                    it.instructions.insertBefore(it.instructions.last.previous, createTextures)
                }

                "loadTickableTexture" -> {
                    val (putTextures) = assembleBlock {
                        aload_0
                        getfield(TextureManager::class, "textures", HashMap::class)
                        aload_1
                        invokevirtual(ResourceLocation::class, "toString", String::class)
                        aload_2
                        invokevirtual(HashMap::class, "put", Object::class, Object::class, Object::class)
                        pop
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is MethodInsnNode && insn.name == "add") {
                            it.instructions.insertBefore(insn.next?.next, putTextures)
                        }
                    }
                }

                "loadTexture" -> {
                    val (createTextureCopy) = assembleBlock {
                        aload_0
                        getfield(TextureManager::class, "textures", HashMap::class)
                        aload_1
                        invokevirtual(ResourceLocation::class, "toString", String::class)
                        invokevirtual(HashMap::class, "get", Object::class, Object::class)
                        checkcast(ITextureObject::class)
                        astore(4)
                        aload(4)
                        ifnull(L["0"])
                        aload(4)
                        astore_2
                        +L["0"]
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is FieldInsnNode && insn.name == "theResourceManager") {
                            it.instructions.insertBefore(insn.previous?.previous, createTextureCopy)
                        }
                    }
                }

                "getDynamicTextureLocation" -> {
                    val (putTextures) = assembleBlock {
                        aload_0
                        getfield(TextureManager::class, "textures", HashMap::class)
                        aload(4)
                        invokevirtual(ResourceLocation::class, "toString", String::class)
                        aload_2
                        invokevirtual(HashMap::class, "put", Object::class, Object::class, Object::class)
                        pop
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn is MethodInsnNode && insn.name == "loadTexture") {
                            it.instructions.insertBefore(insn.previous?.previous?.previous, putTextures)
                        }
                    }
                }
            }
        }

        return original
    }

}
