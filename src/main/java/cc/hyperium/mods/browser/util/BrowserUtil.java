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

package cc.hyperium.mods.browser.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

public class BrowserUtil {

    private static final Map<Integer, Integer> keyModifiers = new HashMap<>();

    static {
        keyModifiers.put(Keyboard.KEY_LSHIFT, 1);
        keyModifiers.put(Keyboard.KEY_RSHIFT, 1);

        keyModifiers.put(Keyboard.KEY_LCONTROL, 2);
        keyModifiers.put(Keyboard.KEY_RCONTROL, 2);

        keyModifiers.put(Keyboard.KEY_LMENU, 3);
        keyModifiers.put(Keyboard.KEY_RMENU, 3);
    }

    public static int getModifierInt() {
        for (Entry<Integer, Integer> entry : keyModifiers.entrySet()) {
            if (Keyboard.isKeyDown(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }

}
