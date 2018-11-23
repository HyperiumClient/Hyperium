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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * Invoked once the player clicks a GUI button
 */
public class ActionPerformedEvent extends CancellableEvent {

    /**
     * The screen which contains the button
     */
    private GuiScreen screen;

    /**
     * The button that was clicked
     */
    private GuiButton button;

    public ActionPerformedEvent(GuiScreen screen, GuiButton button) {
        this.screen = screen;
        this.button = button;
    }

    /**
     * The screen which contains the button
     *
     * @return The screen
     */
    public GuiScreen getScreen() {
        return screen;
    }

    /**
     * The button that was just clicked
     *
     * @return The button
     */
    public GuiButton getButton() {
        return button;
    }
}
