package cc.hyperium.gui;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.settings.items.GeneralSetting;
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
        if(Minecraft.getMinecraft() != null &&
                GeneralSetting.blurGuiBackgroundsEnabled &&
                Minecraft.getMinecraft().entityRenderer != null &&
                Minecraft.getMinecraft().entityRenderer.isShaderActive()) {

            final GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

            if(lastKnownScreen != currentScreen) {
                if(currentScreen == null) // Disable shaders if screen just closed
                    Minecraft.getMinecraft().addScheduledTask(() ->
                            Minecraft.getMinecraft().entityRenderer.stopUseShader());

                lastKnownScreen = Minecraft.getMinecraft().currentScreen;
            }
        }
    }
}
