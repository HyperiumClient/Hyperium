package cc.hyperium.launch.patching.conflicts

import cc.hyperium.cosmetics.companions.hamster.EntityHamster
import cc.hyperium.cosmetics.companions.hamster.RenderHamster
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.entity.RenderManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode

class RenderManagerTransformer : ConflictTransformer {

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "getPosX", double) {
                aload_0
                getfield(RenderManager::class, "o", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }
            method5(public, "getPosY", double) {
                aload_0
                getfield(RenderManager::class, "p", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }
            method5(public, "getPosZ", double) {
                aload_0
                getfield(RenderManager::class, "q", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }
            /*method5(public, "getSkinMap", Map::class) {
                aload_0
                getfield(RenderManager::class, "l", Map::class)
                areturn
                maxStack = 1
                maxLocals = 1
            }*/
        }
        for (method in original.methods) {
            if (method.name == "<init>") {
                val list = assembleBlock {
                    aload_0
                    getfield(RenderManager::class, "k", Map::class)
                    ldc(Type.getType(EntityHamster::class.java))
                    new(RenderHamster::class)
                    dup
                    aload_0
                    invokespecial(RenderHamster::class, "<init>", void, RenderManager::class)
                    invokeinterface(Map::class, "put", Object::class, Object::class, Object::class)
                    pop
                }.first

                method.instructions.iterator().forEach {
                    if (it.opcode == Opcodes.RETURN) {
                        method.instructions.insertBefore(it, list)
                    }
                }
            }
        }
        return original
    }

    override fun getClassName() =  "biu"
}
