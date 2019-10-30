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

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class CCGuiItem {
    private int actionID;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String displayText;
    private GuiScreen currentScreen;
    protected List<String> helpText;

    public CCGuiItem(final GuiScreen screen) {
        this(screen, -1, "no-name", 0, 0, 10, 10);
    }

    public CCGuiItem(final GuiScreen screen, final int id) {
        this(screen, id, "no-name", 0, 0, 10, 10);
    }

    public CCGuiItem(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY, final int width, final int height) {
        actionID = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.displayText = displayText;
        helpText = new ArrayList<>();
        currentScreen = screen;
    }

    public void mouseClicked(final int mouseX, final int mouseY) {
    }

    public void mouseReleased(final int mouseX, final int mouseY) {
    }

    public void drawItem(final int mouseX, final int mouseY) {
    }

    public void setCurrentScreen(final GuiScreen screen) {
        currentScreen = screen;
    }

    public GuiScreen getCurrentScreen() {
        return currentScreen;
    }

    public int getActionID() {
        return actionID;
    }

    public void setPosition(final int newPosX, final int newPosY) {
        posX = newPosX;
        posY = newPosY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(final int newPosX) {
        posX = newPosX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(final int newPosY) {
        posY = newPosY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int newWidth) {
        width = newWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int newHeight) {
        height = newHeight;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(final String newDisplayText) {
        displayText = newDisplayText;
    }

    public List<String> getHelpText() {
        return helpText;
    }
}
