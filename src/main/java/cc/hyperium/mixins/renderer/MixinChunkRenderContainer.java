package cc.hyperium.mixins.renderer;

/*
 * Created by Cubxity on 12/1/2018
 */

import cc.hyperium.mixinsimp.renderer.HyperiumChunkRendererContainer;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderContainer.class)
public class MixinChunkRenderContainer {

    private final HyperiumChunkRendererContainer hyperiumChunkRendererContainer = new HyperiumChunkRendererContainer((ChunkRenderContainer) (Object) this);

    @Inject(method = "preRenderChunk", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;getPosition()Lnet/minecraft/util/BlockPos;"))
    public void preRenderChunk(RenderChunk rc, CallbackInfo ci) {
        hyperiumChunkRendererContainer.preRenderChunk(rc);
    }
}
