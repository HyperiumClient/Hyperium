package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import com.google.common.collect.Maps
import net.minecraft.client.renderer.texture.ITextureObject
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.ResourceLocation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.VarInsnNode

class TextureManagerTransformer : ConflictTransformer {
    override fun getClassName() = "net.minecraft.client.renderer.texture.TextureManager"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(Opcodes.ACC_PRIVATE, "textures", "Ljava/util/HashMap;", null, null).visitEnd()
        original.koffee {
            method5(public, "getMapTextureObjects", Map::class) {
                aload_0
                getfield(TextureManager::class, "mapTextureObjects", Map::class)
                areturn
                maxStack = 1
                maxLocals = 1
            }
        }

        for (method in original.methods) {
            if (method.name == "<init>") {
                val list = assembleBlock {
                    aload_0
                    invokestatic(Maps::class, "newHashMap", HashMap::class)
                    putfield(TextureManager::class, "textures", HashMap::class)
                }.first
                list.add(method.instructions)
                method.instructions = list
            } else if (method.name == "loadTickableTexture") {
                val list = assembleBlock {
                    aload_0
                    getfield(TextureManager::class, "textures", HashMap::class)
                    aload_1
                    invokevirtual(ResourceLocation::class, "toString", String::class)
                    aload_2
                    invokevirtual(HashMap::class, "put", Object::class, Object::class, Object::class)
                }.first
                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.POP) {
                        method.instructions.insert(insn, list)
                        break
                    }
                }
            } else if (method.name == "loadTexture") {
                val list = assembleBlock {
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
                }.first
                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.ISTORE && insn is VarInsnNode && insn.`var` == 3) {
                        method.instructions.insert(insn, list)
                        break
                    }
                }
            } else if (method.name == "getDynamicTextureLocation") {
                val list = assembleBlock {
                    aload_0
                    getfield(TextureManager::class, "textures", HashMap::class)
                    aload(5)
                    invokevirtual(ResourceLocation::class, "toString", String::class)
                    aload_2
                    invokevirtual(HashMap::class, "put", Object::class, Object::class, Object::class)
                    pop
                }.first
                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.ASTORE && insn is VarInsnNode && insn.`var` == 5) {
                        method.instructions.insert(insn, list)
                        break
                    }
                }
            }
        }
        return original
    }
}
