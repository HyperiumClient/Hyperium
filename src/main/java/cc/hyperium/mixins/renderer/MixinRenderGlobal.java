package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.renderer.RenderGlobal;
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
}
