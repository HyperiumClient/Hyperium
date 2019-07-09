package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderEntitiesEvent;
import cc.hyperium.internal.MemoryHelper;
import cc.hyperium.mixins.client.network.IMixinNetworkPlayerInfo;
import cc.hyperium.mixins.entity.IMixinAbstractClientPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks, CallbackInfo info) {
        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().isCancelBox) {
            Hyperium.INSTANCE.getHandlers().getConfigOptions().isCancelBox = false;
            info.cancel();
        }
    }

    @Inject(method = "renderEntities", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    public void renderEnt(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        EventBus.INSTANCE.post(new RenderEntitiesEvent(partialTicks));
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    private void removeEntity(Entity entityIn, CallbackInfo ci) {
        if (entityIn instanceof AbstractClientPlayer) {
            MemoryHelper.INSTANCE.queueDelete(((AbstractClientPlayer) entityIn).getLocationCape());
            MemoryHelper.INSTANCE.queueDelete(((AbstractClientPlayer) entityIn).getLocationSkin());
            NetworkPlayerInfo info = ((IMixinAbstractClientPlayer) entityIn).callGetPlayerInfo();
            if (info == null) return;

            ((IMixinNetworkPlayerInfo) info).setPlayerTexturesLoaded(false);
            ((IMixinNetworkPlayerInfo) info).setLocationCape(null);
            ((IMixinNetworkPlayerInfo) info).setLocationSkin(null);
        }
    }
}
