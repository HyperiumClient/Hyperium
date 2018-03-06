package cc.hyperium.mixins.gui;

import cc.hyperium.gui.settings.items.BackgroundSettings;
import cc.hyperium.gui.settings.items.GeneralSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow protected Minecraft mc;

    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true)
    private void drawWorldBackground(int tint, CallbackInfo ci) {
        if (this.mc.theWorld != null && BackgroundSettings.fastWorldGuiEnabled) {
            ci.cancel();
        }
    }
}
