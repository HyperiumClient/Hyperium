/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mixinsimp.client.renderer.HyperiumEntityRenderer;
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
    public void onTick(TickEvent event) {
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
            if (!Settings.MOTION_BLUR_ENABLED &&
                Settings.BLUR_GUI && mc.entityRenderer.isShaderActive() &&
                mc.currentScreen == null && mc.theWorld != null) {

                HyperiumEntityRenderer.INSTANCE.disableBlurShader();
            }

            // Enable/disable when switching via the settings.
            if (!Settings.MOTION_BLUR_ENABLED && !Settings.BLUR_GUI && prevBlurOption) {
                // Disable GUI blur since the option was just disabled.
                HyperiumEntityRenderer.INSTANCE.disableBlurShader();
            } else if (Settings.BLUR_GUI && !prevBlurOption) {
                // Enable GUI blur since the option was just enabled.
                if (!Settings.MOTION_BLUR_ENABLED) {
                    HyperiumEntityRenderer.INSTANCE.enableBlurShader();
                } else {
                    // Warn user.
                    Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Warning: Background blur will not take effect unless motion blur is disabled.");
                }
            }

            prevBlurOption = Settings.BLUR_GUI;
        }
    }
}
