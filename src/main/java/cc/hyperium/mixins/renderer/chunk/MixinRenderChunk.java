package cc.hyperium.mixins.renderer.chunk;

import cc.hyperium.mixinsimp.renderer.chunk.HyperiumRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Created by Cubxity on 12/1/2018
 */

@Mixin(RenderChunk.class)
public class MixinRenderChunk {

    private HyperiumRenderChunk hyperiumRenderChunk = new HyperiumRenderChunk((RenderChunk) (Object) this);

    @Inject(method = "setPosition", at = @At("INVOKE"))
    public void setPosition(BlockPos bp, CallbackInfo ci) {
        hyperiumRenderChunk.setPosition(bp);
    }
}
