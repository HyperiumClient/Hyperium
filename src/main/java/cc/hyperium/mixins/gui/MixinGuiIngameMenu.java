package cc.hyperium.mixins.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.gui.GuiHyperiumScreenIngameMenu;
import cc.hyperium.mixinsimp.gui.HyperiumGuiIngameMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    private HyperiumGuiIngameMenu hyperiumGuiIngameMenu = new HyperiumGuiIngameMenu((GuiIngameMenu) (Object) this);

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        //hyperiumGuiIngameMenu.initGui(this.buttonList);
        mc.displayGuiScreen(new GuiHyperiumScreenIngameMenu());
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        //hyperiumGuiIngameMenu.actionPerformed(button);
    }

    @Inject(method = "actionPerformed",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/Minecraft;isIntegratedServerRunning()Z"))
    public void quit(GuiButton button, CallbackInfo info) {
        EventBus.INSTANCE.post(new ServerLeaveEvent());
    }
    @Inject(method = "updateScreen", at = @At("HEAD"))
    public void update(CallbackInfo info) {

    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void draw(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        //hyperiumGuiIngameMenu.draw(mouseX, mouseY, partialTicks, fontRendererObj);
    }

}
