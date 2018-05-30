package cc.hyperium.mixins.gui;

import cc.hyperium.gui.GuiHyperiumCredits;
import cc.hyperium.gui.GuiIngameMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(8, this.width - 200, this.height - 20, 200, 20, "Credits"));
//        this.buttonList.get(3).displayString = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? "Change Server" : this.buttonList.get(3).displayString;
//        this.buttonList.get(3).enabled = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? true : this.buttonList.get(3).enabled;
        if (Minecraft.getMinecraft().theWorld.isRemote) {
            GuiButton oldButton = buttonList.remove(3);
            GuiButton newButton = new GuiButton(10, oldButton.xPosition, oldButton.yPosition, oldButton.getButtonWidth(), 20, "Server List");
            buttonList.add(newButton);
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 8)
            Minecraft.getMinecraft().displayGuiScreen(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
        if (button.id == 10 && Minecraft.getMinecraft().theWorld.isRemote)
            Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMultiplayer(Minecraft.getMinecraft().currentScreen));
    }

}
