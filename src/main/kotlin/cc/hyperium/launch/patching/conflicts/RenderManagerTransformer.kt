package cc.hyperium.launch.patching.conflicts

import cc.hyperium.cosmetics.companions.hamster.EntityHamster
import cc.hyperium.cosmetics.companions.hamster.RenderHamster
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.renderer.entity.RenderManager
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode

class RenderManagerTransformer : ConflictTransformer {
    override fun getClassName() = "biu"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.filter {
            it.name == "skinMap" || it.name == "renderPosX" || it.name == "renderPosY" || it.name == "renderPosZ"
        }.forEach {
            it.access = Opcodes.ACC_PUBLIC
        }

        original.methods.find {
            it.name == "<init>"
        }?.apply {
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

            instructions.insertBefore(instructions.last.previous, createHamster)
        }

        return original
    }
}
