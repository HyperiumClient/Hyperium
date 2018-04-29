package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
abstract class MixinGuiEditSign extends GuiScreen {

    /**
     * @see MixinGuiScreen#onGuiClosed(CallbackInfo)
     */
    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo ci) {
        super.onGuiClosed();
    }

    /**
     * @see MixinGuiScreen#initGui(CallbackInfo)
     */
    @Inject(method = "initGui", at = @At("HEAD"))
    private void initGui(CallbackInfo ci) {
        super.initGui();
    }
}
