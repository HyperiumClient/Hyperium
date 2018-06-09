package cc.hyperium.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * Fallback for when GUIs don't disable blurring themselves
 */
public class BlurDisableFallback {

    /**
     * Last screen known to to be displayed to this object
     */
    private GuiScreen lastKnownScreen;

    @InvokeEvent
    private void onTick(TickEvent event) {
        if (Minecraft.getMinecraft() != null &&
                Settings.BLUR_GUI &&
                Minecraft.getMinecraft().entityRenderer != null &&
                Minecraft.getMinecraft().entityRenderer.isShaderActive()) {

            final GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

            if (lastKnownScreen != currentScreen) {
                if (currentScreen == null) // Disable shaders if screen just closed
                    Minecraft.getMinecraft().addScheduledTask(() ->
                            Minecraft.getMinecraft().entityRenderer.stopUseShader());

                lastKnownScreen = Minecraft.getMinecraft().currentScreen;
            }
        }
    }
}
