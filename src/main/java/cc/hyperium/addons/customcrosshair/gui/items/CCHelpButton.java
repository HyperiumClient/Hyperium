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

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.gui.CustomCrosshairScreen;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class CCHelpButton extends CCGuiItem {

    public CCHelpButton(final GuiScreen screen, final List<String> text) {
        super(screen, -1, "", 0, 0, 10, 10);
        helpText = text;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight());
        CustomCrosshairGraphics.drawString("?", getPosX() + 3, getPosY() + 2, 16777215);
        if (mouseX >= getPosX() && mouseX <= getPosX() + getWidth() && mouseY >= getPosY() && mouseY <= getPosY() + getHeight() && getCurrentScreen() instanceof CustomCrosshairScreen) {
            ((CustomCrosshairScreen) getCurrentScreen()).toolTip = getHelpText();
        }
    }
}
