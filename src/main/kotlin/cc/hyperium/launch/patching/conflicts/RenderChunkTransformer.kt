package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.mods.HyperiumModIntegration
import cc.hyperium.mods.chunkanimator.AnimationHandler
import cc.hyperium.mods.chunkanimator.ChunkAnimator
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.renderer.chunk.RenderChunk
import net.minecraft.util.BlockPos
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class RenderChunkTransformer : ConflictTransformer {
    override fun getClassName() = "bht"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(
                Opcodes.ACC_PRIVATE,
                "handler",
                "Lcc/hyperium/mods/chunkanimator/AnimationHandler;",
                null,
                null
        ).visitEnd()

        original.methods.find {
            it.name == "setPosition"
        }?.apply {
            val (animateChunk) = assembleBlock {
                aload_0
                getfield(RenderChunk::class, "handler", AnimationHandler::class)
                ifnonnull(L["1"])
                aload_0
                getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                invokevirtual(Hyperium::class, "getModIntegration", HyperiumModIntegration::class)
                invokevirtual(HyperiumModIntegration::class, "getChunkAnimator", ChunkAnimator::class)
                invokevirtual(ChunkAnimator::class, "getAnimationHandler", AnimationHandler::class)
                putfield(RenderChunk::class, "handler", AnimationHandler::class)
                +L["1"]
                aload_0
                getfield(RenderChunk::class, "handler", AnimationHandler::class)
                aload_0
                aload_1
                invokevirtual(AnimationHandler::class, "setPosition", void, RenderChunk::class, BlockPos::class)
            }

            instructions.insertBefore(instructions.first, animateChunk)
        }

        return original
    }
}
