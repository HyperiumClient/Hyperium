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
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.common.PerspectiveModifierContainer;
import org.lwjgl.input.Keyboard;

public class TogglePerspectiveKeybind extends HyperiumBind {
    public TogglePerspectiveKeybind(){
        super("perspective", Keyboard.KEY_P);
    }

    @Override
    public void onPress() {
        if (Settings.PERSPECTIVE_HOLD) {
            PerspectiveModifierContainer.onEnable();
            PerspectiveModifierContainer.setEnabled(true);
        } else {
            PerspectiveModifierContainer.setEnabled(!PerspectiveModifierContainer.enabled);

            if (!Settings.PERSPECTIVE_HOLD && !PerspectiveModifierContainer.enabled) {
                PerspectiveModifierContainer.onDisable();
            } else {
                PerspectiveModifierContainer.onEnable();
            }
        }
    }

    @Override
    public void onRelease() {
        if (Settings.PERSPECTIVE_HOLD) {
            PerspectiveModifierContainer.onDisable();
        }
    }

}
