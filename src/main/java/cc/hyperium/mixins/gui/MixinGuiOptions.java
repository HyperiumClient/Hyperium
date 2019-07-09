package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class MixinGuiOptions extends GuiScreen {

    private HyperiumGuiOptions hyperiumGuiOptions = new HyperiumGuiOptions((GuiOptions) (Object) this);

    /**
     * @reason Snap done to bottom of the screen
     */
    @Inject(method = "initGui", at = @At(value = "RETURN"))
    public void initGui(CallbackInfo c) {
        hyperiumGuiOptions.initGui(this.buttonList);
    }

    /**
     * @reason Add Hyperium Setting button
     */
    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void actionPerformed(GuiButton button, CallbackInfo c) {
        hyperiumGuiOptions.actionPerformed(button);
    }
}
