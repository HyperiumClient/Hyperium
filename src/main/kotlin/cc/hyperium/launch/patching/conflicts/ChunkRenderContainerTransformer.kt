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
import org.objectweb.asm.tree.FieldNode

class ChunkRenderContainerTransformer : ConflictTransformer {
    override fun getClassName() = "bfh"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.add(FieldNode(Opcodes.ACC_PRIVATE, "handler", "Lcc/hyperium/mods/chunkanimator/AnimationHandler;", null, null))

        for (method in original.methods) {
            val preRender = assembleBlock {
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

            if (method.name == "preRenderChunk") {
                method.instructions.insertBefore(method.instructions.first, preRender)
            }
        }

        return original
    }
}
