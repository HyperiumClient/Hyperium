package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.mods.HyperiumModIntegration
import cc.hyperium.mods.chunkanimator.AnimationHandler
import cc.hyperium.mods.chunkanimator.ChunkAnimator
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.renderer.ChunkRenderContainer
import net.minecraft.client.renderer.chunk.RenderChunk
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ChunkRenderContainerTransformer : ConflictTransformer {
    override fun getClassName() = "bfh"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(
                Opcodes.ACC_PRIVATE,
                "handler",
                "Lcc/hyperium/mods/chunkanimator/AnimationHandler;",
                null,
                null
        ).visitEnd()

        for (method in original.methods) {
            if (method.name == "preRenderChunk") {
                val animateChunk = assembleBlock {
                    aload_0
                    getfield(ChunkRenderContainer::class, "handler", AnimationHandler::class)
                    ifnonnull(L["1"])
                    aload_0
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getModIntegration", HyperiumModIntegration::class)
                    invokevirtual(HyperiumModIntegration::class, "getChunkAnimator", ChunkAnimator::class)
                    invokevirtual(ChunkAnimator::class, "getAnimationHandler", AnimationHandler::class)
                    putfield(ChunkRenderContainer::class, "handler", AnimationHandler::class)
                    +L["1"]
                    aload_0
                    getfield(ChunkRenderContainer::class, "handler", AnimationHandler::class)
                    aload_1
                    invokevirtual(AnimationHandler::class, "preRenderChunk", void, RenderChunk::class)
                }.first

                method.instructions.insertBefore(method.instructions.first, animateChunk)
            }
        }

        return original
    }
}
