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
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiSliderFadeTime extends GuiSlider {

    private final KeystrokesSettings settings;
    private final GuiScreenKeystrokes keystrokesGui;

    public GuiSliderFadeTime(KeystrokesMod mod, int id, int xPos, int yPos, int width, int height, GuiScreenKeystrokes keystrokes) {
        super(id, xPos, yPos, width, height, "Fade Time: ", "%", 0, 30, mod.getSettings().getFadeTime() * 100.0D, false, true);

        settings = mod.getSettings();
        keystrokesGui = keystrokes;
    }

    @Override
    public void updateSlider() {
        super.updateSlider();
        settings.setFadeTime((float) (getValue() / 100.0D));
        keystrokesGui.setUpdated();
        updateDisplayString();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (getValue() > maxValue) setValue(maxValue);
        else if (getValue() < minValue) setValue(minValue);
        updateDisplayString();
        super.drawButton(mc, mouseX, mouseY);
    }

    private void updateDisplayString() {
        displayString = getValue() < 10 ? dispString + "Slow" : getValue() > 20 ? dispString + "Fast" : dispString + "Normal";
    }
}
