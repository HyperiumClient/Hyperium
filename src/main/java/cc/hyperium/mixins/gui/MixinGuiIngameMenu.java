package cc.hyperium.mixins.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.gui.GuiHyperiumScreenIngameMenu;
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
        mc.displayGuiScreen(new GuiHyperiumScreenIngameMenu());
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isIntegratedServerRunning()Z"))
    public void quit(GuiButton button, CallbackInfo info) {
        EventBus.INSTANCE.post(new ServerLeaveEvent());
    }

}
