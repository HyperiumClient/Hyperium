package cc.hyperium.mixinsimp.client.renderer.tileentity;

import cc.hyperium.config.Settings;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumRenderItemFrame {

    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Settings.DISABLE_ITEMFRAMES) {
            ci.cancel();
        }
    }
}
