/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.utils.mods;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeyBindPressEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.mixins.MixinKeyBinding;
import net.minecraft.client.Minecraft;

public class ToggleSprintContainer {

    private static boolean toggleSprintActive = false;

    @InvokeEvent
    public static void onKeyBindPress(KeyBindPressEvent event) {
        if (event.getKeyCode() == KeyBindHandler.toggleSprint.getKey()) {
            if (toggleSprintActive) {
                GeneralChatHandler.instance().sendMessage("ToggleSprint Disabled!");
                ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(false);
            } else {
                GeneralChatHandler.instance().sendMessage("ToggleSprint Enabled!");
                ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
            }
            toggleSprintActive = !toggleSprintActive;
        }
    }

    @InvokeEvent
    public static void onKeyPress(KeypressEvent event) {
        if (toggleSprintActive) {
            if (event.getKey() == Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode() || event.getKey() == KeyBindHandler.toggleSprint.getKey()) {
                ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
            }
        }
    }

}
