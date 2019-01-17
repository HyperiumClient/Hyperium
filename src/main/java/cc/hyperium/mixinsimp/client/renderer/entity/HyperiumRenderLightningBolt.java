package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.config.Settings;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumRenderLightningBolt {

    public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Settings.DISABLE_LIGHTNING) {
            ci.cancel();
        }
    }
}
