package com.hcc.mixins.world;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunk {

    /**
     * Used in fullbright module
     *
     * @param ci
     * @param type
     * @param pos
     */
    @Inject(method = "getLightFor", at = @At("HEAD"), cancellable = true)
    private void getLightFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            ci.setReturnValue(15);
        }
    }


    /**
     * Used in fullbright module
     *
     * @param pos
     * @param amount
     */
    @Inject(method = "getLightSubtracted", at = @At("HEAD"), cancellable = true)
    private void getLightSubtracted(BlockPos pos, int amount, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            ci.setReturnValue(15);
        }
    }
}
