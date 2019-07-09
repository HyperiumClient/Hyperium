/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.gui.CustomCrosshairScreen;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class CCHelpButton extends CCGuiItem {

    public CCHelpButton(final GuiScreen screen, final List<String> text) {
        super(screen, -1, "", 0, 0, 10, 10);
        this.helpText = text;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        CustomCrosshairGraphics.drawString("?", this.getPosX() + 3, this.getPosY() + 2, 16777215);
        if (mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + this.getHeight() && this.getCurrentScreen() instanceof CustomCrosshairScreen) {
            ((CustomCrosshairScreen) this.getCurrentScreen()).toolTip = this.getHelpText();
        }
    }
}
