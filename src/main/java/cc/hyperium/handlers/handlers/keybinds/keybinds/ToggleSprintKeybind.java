/*
 *      Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published
 *      by the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mixins.MixinKeyBinding;
import cc.hyperium.mods.common.ToggleSprintContainer;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ToggleSprintKeybind extends HyperiumBind {

    public ToggleSprintKeybind() {
        super("toggleSprint", Keyboard.KEY_V);
    }

    @Override
    public void onPress() {
        if (ToggleSprintContainer.toggleSprintActive) {
            if (Settings.SPRINT_PERSPECTIVE_MESSAGES) {
                GeneralChatHandler.instance().sendMessage("ToggleSprint Disabled!");
            }
            ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(false);
        } else {
            if (Settings.SPRINT_PERSPECTIVE_MESSAGES) {
                GeneralChatHandler.instance().sendMessage("ToggleSprint Enabled!");
            }
            ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
        }
        ToggleSprintContainer.toggleSprintActive = !ToggleSprintContainer.toggleSprintActive;
    }
}
