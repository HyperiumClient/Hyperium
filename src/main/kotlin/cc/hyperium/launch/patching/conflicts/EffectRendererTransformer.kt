package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm.aload_0
import codes.som.anthony.koffee.insns.jvm.areturn
import codes.som.anthony.koffee.insns.jvm.getfield
import codes.som.anthony.koffee.koffee
import net.minecraft.client.particle.EffectRenderer
import org.objectweb.asm.tree.ClassNode

class EffectRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bec"

    override fun transform(original: ClassNode): ClassNode {
        original.koffee {
            method5(public, "getParticleMap", Map::class) {
                aload_0
                getfield(EffectRenderer::class, "particleTypes", Map::class)
                areturn
                maxStack = 1
                maxLocals = 1
            }
        }

        return original
    }
}
