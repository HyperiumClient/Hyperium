package cc.hyperium.launch.patching.conflicts

import cc.hyperium.cosmetics.companions.hamster.EntityHamster
import cc.hyperium.cosmetics.companions.hamster.RenderHamster
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.renderer.entity.RenderManager
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode

class RenderManagerTransformer : ConflictTransformer {
    override fun getClassName() = "biu"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "getPosX", double) {
                aload_0
                getfield(RenderManager::class, "renderPosX", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }

            method5(public, "getPosY", double) {
                aload_0
                getfield(RenderManager::class, "renderPosY", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }

            method5(public, "getPosZ", double) {
                aload_0
                getfield(RenderManager::class, "renderPosZ", double)
                dreturn
                maxStack = 2
                maxLocals = 1
            }
        }

        for (method in original.methods) {
            if (method.name == "<init>") {
                val createHamster = assembleBlock {
                    aload_0
                    getfield(RenderManager::class, "entityRenderMap", Map::class)
                    ldc(Type.getType(EntityHamster::class.java))
                    new(RenderHamster::class)
                    dup
                    aload_0
                    invokespecial(RenderHamster::class, "<init>", void, RenderManager::class)
                    invokeinterface(Map::class, "put", Object::class, Object::class, Object::class)
                    pop
                    _return
                }.first

                method.instructions.insertBefore(method.instructions.last, createHamster)
            }
        }

        return original
    }
}
