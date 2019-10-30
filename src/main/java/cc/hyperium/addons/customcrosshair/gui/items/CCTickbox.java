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

import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class CCTickbox extends CCGuiItem {

    private CCCallback callback;

    private boolean checked;

    public CCTickbox(final GuiScreen screen) {
        super(screen);
        setWidth(10);
        setHeight(10);
    }

    public CCTickbox(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY) {
        super(screen, id, displayText, posX, posY, 10, 10);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        toggleChecked();

        if (callback != null) {
            callback.run(checked);
        }
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawThemeBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight());
        CustomCrosshairGraphics
            .drawString(getDisplayText(), getPosX() + getWidth() + 3, getPosY() + getHeight() / 2 - 3, 16777215);
        if (checked) {
            CustomCrosshairGraphics
                .drawFilledRectangle(getPosX() + 2, getPosY() + 2, getPosX() + getWidth() - 1, getPosY() + getHeight() - 1, new Color(50, 255, 50, 255));
        }
    }

    public CCTickbox setCallback(CCCallback callback) {
        this.callback = callback;

        return this;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(final boolean newChecked) {
        checked = newChecked;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}
