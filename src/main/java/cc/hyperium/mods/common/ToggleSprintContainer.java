/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.mods.common;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.mixins.MixinKeyBinding;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ToggleSprintContainer {

    boolean press = false;
    private boolean toggleSprintActive = false;
    private final HyperiumBind toggleSprint = new HyperiumBind("toggleSprint", Keyboard.KEY_V) {
        @Override
        public void onPress() {
            press = !press;
            if (press) {
                if (ToggleSprintContainer.this.toggleSprintActive) {
                    GeneralChatHandler.instance().sendMessage("ToggleSprint Disabled!");
                    ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(false);
                } else {
                    GeneralChatHandler.instance().sendMessage("ToggleSprint Enabled!");
                    ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
                }
                ToggleSprintContainer.this.toggleSprintActive = !ToggleSprintContainer.this.toggleSprintActive;
            }
        }
    };

    public ToggleSprintContainer() {
        KeyBindHandler keyBindHandler = Hyperium.INSTANCE.getHandlers().getKeybindHandler();

        if (keyBindHandler.getBinding(this.toggleSprint.getKeyDescription()) == null) {
            keyBindHandler.registerKeyBinding(this.toggleSprint);
        }
    }

    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (this.toggleSprintActive) {
            if (event.getKey() == Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode() || event.getKey() == this.toggleSprint.getKeyCode()) {
                ((MixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
            }
        }
    }

}
