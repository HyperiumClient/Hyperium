package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumWorld {

    public void setSpawnPoint(BlockPos pos) {
        EventBus.INSTANCE.post(new SpawnpointChangeEvent(pos));
    }

    public void checkLightFor(CallbackInfoReturnable<Boolean> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(false);
        }
    }

    public void getLightFromNeighborsFor(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public double getHorizon(WorldInfo worldInfo) {
        if (Settings.VOID_FLICKER_FIX) {
            return 0.0;
        }

        return worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    public void getLightFromNeighbor(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getRawLight(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getLight(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }
}
