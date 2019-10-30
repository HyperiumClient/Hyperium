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

package cc.hyperium.mods.common;

import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.Minecraft;

/**
 * The perspective mod handler
 */
public class PerspectiveModifierHandler {

    /**
     * This class requires massive improvements, this will be worked on and fixed in a later release
     */
    public float modifiedYaw;
    public float modifiedPitch;

    public boolean enabled;

    public void onEnable() {
        enabled = true;
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
            modifiedYaw = Minecraft.getMinecraft().thePlayer.rotationYaw + 180.0F;
            modifiedPitch = -Minecraft.getMinecraft().thePlayer.rotationPitch;
        } else {
            modifiedPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
            modifiedYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        }

        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;

        if (Settings.SPRINT_PERSPECTIVE_MESSAGES) {
            GeneralChatHandler.instance().sendMessage("Enabled 360 Degree Perspective.");
        }
    }

    public void onDisable() {
        // Prevents being disabled twice by different methods.
        if (!enabled) return;

        enabled = false;

        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        if (Settings.SPRINT_PERSPECTIVE_MESSAGES) {
            GeneralChatHandler.instance().sendMessage("Disabled 360 Degree Perspective.");
        }

        // Reset the states anyway
        modifiedYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        modifiedPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
    }
}
