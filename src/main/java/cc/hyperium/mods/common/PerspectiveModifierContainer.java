/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.common;

import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.client.Minecraft;

/**
 * The perspective mod handler
 */
public class PerspectiveModifierContainer {

    /**
     * This class requires massive improvements, this will be worked on and fixed in a later release
     */
    public static float modifiedYaw;
    public static float modifiedPitch;

    public static boolean enabled = false;

    public static void onEnable() {
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
        GeneralChatHandler.instance().sendMessage("Enabled 360 Degree Perspective.");

        modifiedYaw = Minecraft.getMinecraft().thePlayer.cameraYaw;
        modifiedPitch = Minecraft.getMinecraft().thePlayer.cameraPitch;
    }

    public static void onDisable() {
        PerspectiveModifierContainer.enabled = false;

        //((MixinKeyBinding) this.perspective).setPressed(false);

        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        GeneralChatHandler.instance().sendMessage("Disabled 360 Degree Perspective.");

        // Reset the states anyway
        modifiedYaw = Minecraft.getMinecraft().thePlayer.cameraYaw;
        modifiedPitch = Minecraft.getMinecraft().thePlayer.cameraPitch;
    }

    public static void setEnabled(boolean enabled) {
        PerspectiveModifierContainer.enabled = enabled;
    }

}
