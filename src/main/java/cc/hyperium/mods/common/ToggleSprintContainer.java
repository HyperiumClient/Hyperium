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

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mixins.client.settings.IMixinKeyBinding;
import net.minecraft.client.Minecraft;

public class ToggleSprintContainer {

    @ConfigOpt
    public static boolean toggleSprintActive;

    @InvokeEvent
    public void onTick(TickEvent e) {
        if (toggleSprintActive) {
            ((IMixinKeyBinding) Minecraft.getMinecraft().gameSettings.keyBindSprint).setPressed(true);
        }
    }
}
