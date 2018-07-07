package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumRenderPlayer {
    private RenderPlayer parent;

    public HyperiumRenderPlayer(RenderPlayer parent) {
        this.parent = parent;
    }

    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci, RenderManager renderManager) {
        if (Settings.TURN_PEOPLE_INTO_BLOCKS) {
            try {
                ci.cancel();
                Hyperium.INSTANCE.getHandlers().getRenderPlayerAsBlock().reDraw(entity, x, y, z);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            EventBus.INSTANCE.post(new RenderPlayerEvent(entity, renderManager, x, y, z, partialTicks));
    }
    public void onUpdateTimer(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        ModelPlayer modelplayer = parent.getMainModel();
        modelplayer.isRiding = modelplayer.isSneak = false;
    }
}
