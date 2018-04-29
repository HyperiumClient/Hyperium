package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
abstract class MixinGuiContainer extends GuiScreen {

    /**
     * @see MixinGuiScreen#onGuiClosed(CallbackInfo)
     */
    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo ci) {
        super.onGuiClosed();
    }
}
