package cc.hyperium.mixins.gui;

import cc.hyperium.gui.GuiHyperiumCredits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(8, this.width - 200, this.height - 20, 200, 20, "Credits"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 8)
            Minecraft.getMinecraft().displayGuiScreen(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
    }

}
