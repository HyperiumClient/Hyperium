package cc.hyperium.mixins.renderer.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.mods.itemphysic.physics.ClientPhysic;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KodingKing
 */
@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        if (Settings.ITEM_PHYSIC_ENABLED) {
            ClientPhysic.doRender(entity, x, y, z, entityYaw, partialTicks);
            callbackInfo.cancel();
        }
    }

}
