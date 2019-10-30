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

package cc.hyperium.event.gui;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.client.gui.GuiScreen;

/**
 * Invoked when a user clicks within a GuiScreen.
 *
 * @param mouseX x position of the mouse on click
 * @param mouseY y position of the mouse on click
 * @param button Mouse button clicked (0 = left, 1 = right, 2 = middle)
 * @param gui    GUI that detected the click
 */
public class GuiClickEvent extends CancellableEvent {

    private int mouseX;
    private int mouseY;
    private int button;

    private GuiScreen gui;

    /**
     * Invoked when a user clicks within a GuiScreen.
     *
     * @param mouseX x position of the mouse on click
     * @param mouseY y position of the mouse on click
     * @param button Mouse button clicked (0 = left, 1 = right, 2 = middle)
     * @param gui    GUI that detected the click
     */
    public GuiClickEvent(int mouseX, int mouseY, int button, GuiScreen gui) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.gui = gui;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getButton() {
        return button;
    }

    public GuiScreen getGui() {
        return gui;
    }
}
