package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {

    @Shadow
    protected abstract boolean checkHotbarKeys(int keyCode);

    /**
     * @see MixinGuiScreen#onGuiClosed(CallbackInfo)
     */
    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo ci) {
        super.onGuiClosed();
    }

    /**
     * @author Sk1er
     * @reason "Inventory Mouse Fix" method
     */
    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        checkHotbarKeys(mouseButton - 100);
    }
}
