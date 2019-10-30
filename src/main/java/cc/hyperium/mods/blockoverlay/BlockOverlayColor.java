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

package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class BlockOverlayColor extends GuiScreen {
    private BlockOverlay mod;
    private GuiButton buttonChroma;
    private GuiButton buttonBack;
    private GuiSlider sliderChroma;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;

    public BlockOverlayColor(BlockOverlay mod) {
        this.mod = mod;
    }

    public void initGui() {
        super.buttonList.add(buttonChroma = new GuiButton(0, super.width / 2 - 50, super.height / 2 - 60, 100, 20,
            "Chroma: " + (mod.getSettings().isChroma() ? "On" : "Off")));
        super.buttonList.add(sliderChroma = new GuiSlider(1, super.width / 2 - 50, super.height / 2 - 35, 100, 20,
            "Speed: ", "", 1, 5, mod.getSettings().getChromaSpeed(), false, true));
        super.buttonList.add(sliderRed = new GuiSlider(2, super.width / 2 - 50, super.height / 2 - 35, 100, 20,
            "Red: ", "", 0, 255, mod.getSettings().getOverlayRed() * 255, false, true));
        super.buttonList.add(sliderGreen = new GuiSlider(3, super.width / 2 - 50, super.height / 2 - 10, 100, 20,
            "Green: ", "", 0, 255, mod.getSettings().getOverlayGreen() * 255, false, true));
        super.buttonList.add(sliderBlue = new GuiSlider(4, super.width / 2 - 50, super.height / 2 + 15, 100, 20,
            "Blue: ", "", 0, 255, mod.getSettings().getOverlayBlue() * 255, false, true));
        super.buttonList.add(sliderAlpha = new GuiSlider(5, super.width / 2 - 50, super.height / 2 + 40, 100, 20,
            "Alpha: ", "", 0, 255, mod.getSettings().getOverlayAlpha() * 255, false, true));
        super.buttonList.add(buttonBack = new GuiButton(6, super.width / 2 - 50, super.height / 2 + 65, 100, 20,
            "Back"));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawDefaultBackground();
        if (mod.getSettings().isChroma()) {
            sliderRed.enabled = false;
            sliderGreen.enabled = false;
            sliderBlue.enabled = false;
            sliderAlpha.yPosition = super.height / 2 - 10;
            sliderChroma.enabled = true;
            sliderChroma.drawButton(super.mc, mouseX, mouseY);
        } else {
            sliderRed.enabled = true;
            sliderGreen.enabled = true;
            sliderBlue.enabled = true;
            sliderAlpha.yPosition = super.height / 2 + 40;
            sliderChroma.enabled = false;
            sliderRed.drawButton(super.mc, mouseX, mouseY);
            sliderGreen.drawButton(super.mc, mouseX, mouseY);
            sliderBlue.drawButton(super.mc, mouseX, mouseY);
        }
        buttonChroma.drawButton(super.mc, mouseX, mouseY);
        sliderAlpha.drawButton(super.mc, mouseX, mouseY);
        buttonBack.drawButton(super.mc, mouseX, mouseY);
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                mod.getSettings().setChroma(!mod.getSettings().isChroma());
                buttonChroma.displayString = "Chroma: " + (mod.getSettings().isChroma() ? "On" : "Off");
                break;
            case 1:
                mod.getSettings().setChromaSpeed(sliderChroma.getValueInt());
                break;
            case 2:
                mod.getSettings().setOverlayRed(sliderRed.getValueInt() / 255.0f);
                break;
            case 3:
                mod.getSettings().setOverlayGreen(sliderGreen.getValueInt() / 255.0f);
                break;
            case 4:
                mod.getSettings().setOverlayBlue(sliderBlue.getValueInt() / 255.0f);
                break;
            case 5:
                mod.getSettings().setOverlayAlpha(sliderAlpha.getValueInt() / 255.0f);
                break;
            case 6:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new BlockOverlayGui(mod));
                break;
        }
    }

    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        mod.getSettings().setChromaSpeed(sliderChroma.getValueInt());
        mod.getSettings().setOverlayRed(sliderRed.getValueInt() / 255.0f);
        mod.getSettings().setOverlayGreen(sliderGreen.getValueInt() / 255.0f);
        mod.getSettings().setOverlayBlue(sliderBlue.getValueInt() / 255.0f);
        mod.getSettings().setOverlayAlpha(sliderAlpha.getValueInt() / 255.0f);
    }

    public void onGuiClosed() {
        mod.getSettings().save();
    }
}
