package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.event.minigames.MinigameListener;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumRenderLightningBolt {

    private MinigameListener listener = new MinigameListener();

    public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Settings.DISABLE_LIGHTNING && !Settings.UHC_LIGHTNING && isInUHC(listener)) {
            ci.cancel();
        }
    }

    private boolean isInUHC(MinigameListener listener) {
        return listener.getCurrentMinigameName().equalsIgnoreCase("UHC CHAMPIONS".toLowerCase());
    }
}
