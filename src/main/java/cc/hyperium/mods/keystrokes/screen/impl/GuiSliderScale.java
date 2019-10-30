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

package cc.hyperium.mods.keystrokes.screen.impl;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderScale extends GuiSlider {

    private final KeystrokesSettings settings;
    private final GuiScreenKeystrokes keystrokesGui;

    public GuiSliderScale(KeystrokesMod mod, int id, int xPos, int yPos, int width, int height, GuiScreenKeystrokes keystrokes) {
        super(id, xPos, yPos, width, height, "Scale: ", "%", 50, 150, mod.getSettings().getScale() * 100.0D, false, true);

        settings = mod.getSettings();
        keystrokesGui = keystrokes;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        settings.setX(0);
        settings.setY(0);
        settings.setScale((float) (getValue() / 100.0D));
        keystrokesGui.setUpdated();
    }
}
