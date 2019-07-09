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

package cc.hyperium.mods.browser.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.browser.gui.GuiBrowser;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

/**
 * @author Koding
 */
public class BrowserBind extends HyperiumBind {

    public BrowserBind() {
        super("Open Browser", Keyboard.KEY_G);
    }

    @Override
    public void onPress() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBrowser)
            return;
        Hyperium.INSTANCE.getModIntegration().getBrowserMod().showBrowser();
    }
}
