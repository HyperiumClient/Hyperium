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
    private boolean initialBlur = Settings.BLUR_GUI;
    private boolean loadedBlur = false;

    @InvokeEvent
    private void onTick(TickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Clear shaders on disable.
        if (mc != null && mc.entityRenderer != null) {
            if(!loadedBlur){
                // Loads blur setting for first time.
                loadedBlur = true;
                initialBlur = Settings.BLUR_GUI;
            }
            if(!Settings.BLUR_GUI && Settings.BLUR_GUI != initialBlur){
                // Blur was just disabled.
                mc.addScheduledTask(() ->
                    mc.entityRenderer.stopUseShader());
            }

            initialBlur = Settings.BLUR_GUI;
        }

        if (mc != null && Settings.BLUR_GUI && mc.entityRenderer != null && mc.entityRenderer.isShaderActive()) {
            GuiScreen currentScreen = mc.currentScreen;

            if (lastKnownScreen != currentScreen) {
                if (currentScreen == null) { // Disable shaders if screen just closed
                    mc.addScheduledTask(() ->
                        mc.entityRenderer.stopUseShader());
                }

                lastKnownScreen = mc.currentScreen;
            }
        }
    }
}
