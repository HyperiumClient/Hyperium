/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.mods;


import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeyBindPressEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import net.minecraft.client.Minecraft;

public class PerspectiveModifierContainer {
    public boolean enabled = false;

    public float modifiedYaw;
    public float modifiedPitch;

    public void onEnable() {
        GeneralChatHandler.instance().sendMessage("Enabled 360 Degree Perspective.");
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;

        modifiedYaw = Minecraft.getMinecraft().thePlayer.cameraYaw;
        modifiedPitch = Minecraft.getMinecraft().thePlayer.cameraPitch;
    }

    public void onDisable() {
        this.enabled = false;
        Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        GeneralChatHandler.instance().sendMessage("Disabled 360 Degree Perspective.");
    }

    @InvokeEvent
    public void onKeyBindPress(KeyBindPressEvent event) {
        if (event.getKeyCode() == KeyBindHandler.perspective.getKey()) {
            enabled = !enabled;
            if (!enabled) {
                onDisable();
            } else {
                onEnable();
            }

        }
    }
}
