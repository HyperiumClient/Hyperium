package cc.hyperium.mixinsimp.renderer;

/*
 * Created by Cubxity on 12/1/2018
 */

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chunkanimator.AnimationHandler;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class HyperiumChunkRendererContainer {
    private ChunkRenderContainer parent;
    private AnimationHandler handler;

    public HyperiumChunkRendererContainer(ChunkRenderContainer parent) {
        this.parent = parent;
    }


    public void preRenderChunk(RenderChunk rc) {
        if (handler == null)
            handler = Hyperium.INSTANCE.getModIntegration().getChunkAnimator().getAnimationHandler();
        handler.preRenderChunk(rc);
    }
}
