package cc.hyperium.mixins.gui;

import cc.hyperium.gui.ParticleOverlay;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinInventoryParticle {
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"))
    public void draw(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ParticleOverlay.getOverlay().render(mouseX, mouseY);
    }
}
