package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class MixinGuiOptions extends GuiScreen {

    /**
     * @reason Snap done to bottom of the screen
     */
    @Inject(method = "initGui", at = @At(value = "RETURN"))
    public void initGui(CallbackInfo c) {
        this.buttonList.forEach(b -> {
            if (b.id == 200) {
                b.yPosition = this.height - 30;
            }
        });
    }
}
