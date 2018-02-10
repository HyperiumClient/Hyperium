package com.hcc.mixins.world;

import com.hcc.event.EventBus;
import com.hcc.event.SpawnpointChangeEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {

    /**
     * Invoked once the server changes the players spawn point
     * @param pos the new spawn position
     * @param ci {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "setSpawnPoint", at=@At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci){
        EventBus.INSTANCE.post(new SpawnpointChangeEvent(pos));
    }
}
