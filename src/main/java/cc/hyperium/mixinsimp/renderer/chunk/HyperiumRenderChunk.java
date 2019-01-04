package cc.hyperium.mixinsimp.renderer.chunk;

/*
 * Created by Cubxity on 12/1/2018
 */

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chunkanimator.AnimationHandler;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;

public class HyperiumRenderChunk {
    private RenderChunk parent;
    private AnimationHandler handler;

    public HyperiumRenderChunk(RenderChunk parent) {
        this.parent = parent;
    }

    public void setPosition(BlockPos bp) {
        if (handler == null)
            handler = Hyperium.INSTANCE.getModIntegration().getChunkAnimator().getAnimationHandler();
        handler.setPosition(parent, bp);
    }
}
