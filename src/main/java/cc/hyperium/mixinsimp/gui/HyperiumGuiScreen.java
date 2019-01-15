package cc.hyperium.mixinsimp.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumGuiScreen {
    private GuiScreen parent;

    public HyperiumGuiScreen(GuiScreen parent) {
        this.parent = parent;
    }

    public void drawWorldBackground(int tint, Minecraft mc, CallbackInfo ci) {
        if (mc.theWorld != null && Settings.FAST_CONTAINER) {
            ci.cancel();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        GuiClickEvent event = new GuiClickEvent(mouseX, mouseY, mouseButton, parent);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    public boolean actionPerformed(GuiButton button) {
        ActionPerformedEvent event = new ActionPerformedEvent(parent, button);
        EventBus.INSTANCE.post(event);
        return event.isCancelled();
    }

    public void initGui() {
        EventBus.INSTANCE.post(new InitGuiEvent(parent));
    }

    public void keyTyped(char typedChar, int keyCode) {
        EventBus.INSTANCE.post(new GuiKeyTypedEvent(parent, typedChar, keyCode));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new GuiDrawScreenEvent(parent, mouseX, mouseY, partialTicks));
    }

    public void onGuiClosed(CallbackInfo ci) {
    }
}
