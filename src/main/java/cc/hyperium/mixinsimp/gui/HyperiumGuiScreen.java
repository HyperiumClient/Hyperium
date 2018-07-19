package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiClickEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
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

    public void initGui() {
        Hyperium.INSTANCE.getHandlers().getKeybindHandler().releaseAllKeybinds();
        if (Settings.BLUR_GUI && !Settings.FAST_CONTAINER) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Method loadShaderMethod = null;
                try {
                    loadShaderMethod = EntityRenderer.class.getDeclaredMethod("loadShader", ResourceLocation.class);
                } catch (NoSuchMethodException e) {
                    try {
                        loadShaderMethod = EntityRenderer.class.getDeclaredMethod("a", ResourceLocation.class);
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    }
                }

                if (loadShaderMethod != null) {
                    loadShaderMethod.setAccessible(true);
                    try {
                        loadShaderMethod.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("shaders/hyperium_blur.json"));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onGuiClosed(CallbackInfo ci) {
        if(Settings.BLUR_GUI) {
            Minecraft.getMinecraft()
                .addScheduledTask(() -> Minecraft.getMinecraft().entityRenderer.stopUseShader());
        }
    }
}
