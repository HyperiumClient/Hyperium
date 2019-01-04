package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

/**
 * Handles GUI Blurring.
 */
public class BlurHandler {

    private boolean prevBlurOption;

    public BlurHandler() {
        prevBlurOption = Settings.BLUR_GUI;
    }

    @InvokeEvent
    private void onTick(TickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.entityRenderer != null) {

            // Enable the blur if criteria is met.
            if (!Settings.MOTION_BLUR_ENABLED &&
                Settings.BLUR_GUI && mc.currentScreen != null &&
                !(mc.currentScreen instanceof GuiChat) &&
                !mc.entityRenderer.isShaderActive() && mc.theWorld != null) {

                HyperiumEntityRenderer.INSTANCE.enableBlurShader();
            }

            // Disable the blur if criteria is met.
            if(!Settings.MOTION_BLUR_ENABLED &&
                Settings.BLUR_GUI && mc.entityRenderer.isShaderActive() &&
                mc.currentScreen == null && mc.theWorld != null){
                
                HyperiumEntityRenderer.INSTANCE.disableBlurShader();
            }

            // Enable/disable when switching via the settings.
            if (!Settings.MOTION_BLUR_ENABLED && Settings.BLUR_GUI == false && prevBlurOption == true) {
                // Disable GUI blur since the option was just disabled.
                HyperiumEntityRenderer.INSTANCE.disableBlurShader();
            } else if (Settings.BLUR_GUI == true && prevBlurOption == false) {
                // Enable GUI blur since the option was just enabled.
                if(!Settings.MOTION_BLUR_ENABLED) {
                    HyperiumEntityRenderer.INSTANCE.enableBlurShader();
                } else{
                    // Warn user.
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Warning: Background blur will not take effect unless motion blur is disabled.",true);
                }
            }
            prevBlurOption = Settings.BLUR_GUI;
        }
    }
}
