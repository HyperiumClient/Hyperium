/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class KeyBindHandler {

    public static ArrayList<HyperiumBind> keybinds = new ArrayList<>();

    public static HyperiumBind toggleSprint = new HyperiumBind("toggleSprint", 47);
    public static HyperiumBind debug = new HyperiumBind("debug", Keyboard.KEY_L);

    public KeyBindHandler() {
        keybinds.add(toggleSprint);
        keybinds.add(debug);
    }

    @InvokeEvent
    public static void onKeyPress(KeypressEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : keybinds) {
                if (event.getKey() == bind.getKey()) {
                    bind.onPress();
                }
            }
        }
    }
}
