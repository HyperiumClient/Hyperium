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

package cc.hyperium.event;

import net.minecraft.client.gui.GuiScreen;

/**
 * Invoked when a character is typed in a GUI screen, such as {@link net.minecraft.client.gui.GuiChat}.
 */
public class GuiKeyTypedEvent {

    /**
     * The GUI screen
     */
    private GuiScreen screen;

    /**
     * The character that was just typed
     */
    private char typedChar;

    /**
     * The key code of the character
     */
    private int keyCode;

    public GuiKeyTypedEvent(GuiScreen screen, char typedChar, int keyCode) {
        this.screen = screen;
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public char getTypedChar() {
        return typedChar;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
