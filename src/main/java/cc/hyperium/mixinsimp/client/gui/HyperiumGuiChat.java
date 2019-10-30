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

package cc.hyperium.mixinsimp.client.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.HypixelDetector;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;

public class HyperiumGuiChat {

    public void onSendAutocompleteRequest(String leftOfCursor) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().autoComplete(leftOfCursor);
    }

    public void init(GuiTextField inputField) {
        inputField.setMaxStringLength(HypixelDetector.getInstance().isHypixel() ? 256 : 100);
    }
}
