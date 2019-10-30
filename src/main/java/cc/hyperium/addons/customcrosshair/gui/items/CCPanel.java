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

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class CCPanel extends CCGuiItem {
    private int contentWidth;
    private int contentHeight;
    private boolean verticalScroll;
    private int verticalScrollPosition;
    private boolean verticalScrollMouseDown;
    private boolean horizontalScroll;
    private int horizontalScrollPosition;
    private boolean horizontalScrollMouseDown;
    private int scrollSize;
    private int boxSize;

    public CCPanel(final GuiScreen screen, final int id, final int x, final int y, final int width, final int height, final int cWidth, final int cHeight) {
        super(screen, id, "", x, y, width, height);
        contentWidth = cWidth;
        contentHeight = cHeight;
        if (contentHeight > getHeight()) {
            verticalScroll = true;
        }
        if (contentWidth > getWidth()) {
            horizontalScroll = true;
        }
        scrollSize = 10;
        boxSize = 30;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        CustomCrosshairGraphics
            .drawBorderedRectangle(getPosX(), getPosY(), getPosX() + getWidth(), getPosY() + getHeight(), new Color(255, 255, 255, 32), CustomCrosshairAddon.SECONDARY);
        if (verticalScrollMouseDown) {
            verticalScrollPosition = mouseY - getPosY();
            if (verticalScrollPosition < 0) {
                verticalScrollPosition = 0;
            }
            if (verticalScrollPosition > getHeight() - scrollSize - boxSize) {
                verticalScrollPosition = getHeight() - scrollSize - boxSize;
            }
        }
        if (verticalScroll) {
            CustomCrosshairGraphics
                .drawThemeBorderedRectangle(getPosX() + getWidth() - scrollSize, getPosY(), getPosX() + getWidth(), getPosY() + getHeight() - (horizontalScroll ? scrollSize : 0));
            CustomCrosshairGraphics
                .drawThemeBorderedRectangle(getPosX() + getWidth() - scrollSize, getPosY() + verticalScrollPosition, getPosX() + getWidth(), getPosY() + verticalScrollPosition + boxSize);
        }
        if (horizontalScroll) {
            CustomCrosshairGraphics
                .drawThemeBorderedRectangle(getPosX(), getPosY() + getHeight() - scrollSize, getPosX() + getWidth() - (verticalScroll ? scrollSize : 0), getPosY() + getHeight());
            CustomCrosshairGraphics
                .drawThemeBorderedRectangle(getPosX() + horizontalScrollPosition, getPosY() + getHeight() - scrollSize, getPosX() + horizontalScrollPosition + boxSize, getPosY() + getHeight());
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (verticalScroll && mouseX >= getPosX() + getWidth() - scrollSize && mouseX < getPosX() + getWidth() && mouseY >= getPosY() + verticalScrollPosition && mouseY <= getPosY() + verticalScrollPosition + boxSize) {
            verticalScrollMouseDown = true;
        }
        if (horizontalScroll && mouseX >= getPosX() + horizontalScrollPosition && mouseX <= getPosX() + horizontalScrollPosition + boxSize && mouseY >= getPosY() + getHeight() - scrollSize && mouseY <= getPosY() + getHeight()) {
            horizontalScrollMouseDown = true;
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        verticalScrollMouseDown = false;
        horizontalScrollMouseDown = false;
    }
}
