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

package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.mods.keystrokes.KeystrokesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiScreenColor extends GuiScreen implements IScreen {

    private final KeystrokesMod mod; // OOP

    private final IScrollable red;
    private final IScrollable green;
    private final IScrollable blue;
    private final IScrollable pressedRed;
    private final IScrollable pressedGreen;
    private final IScrollable pressedBlue;

    private boolean updated = false; // Have the values been changed?

    GuiScreenColor(KeystrokesMod mod, IScrollable red, IScrollable green, IScrollable blue, IScrollable pressedRed, IScrollable pressedGreen, IScrollable pressedBlue) {
        this.mod = mod;

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.pressedRed = pressedRed;
        this.pressedGreen = pressedGreen;
        this.pressedBlue = pressedBlue;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiSlider(0, width / 2 - 150, calculateHeight(3), 150, 20, "Red: ", "",
            0, 255, red.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                red.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiSlider(1, width / 2 - 150, calculateHeight(4), 150, 20, "Green: ", "",
            0, 255, green.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                green.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiSlider(2, width / 2 - 150, calculateHeight(5), 150, 20, "Blue: ", "",
            0, 255, blue.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                blue.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiSlider(3, width / 2 + 5, calculateHeight(3), 150, 20, "Pressed Red: ", "",
            0, 255, pressedRed.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                pressedRed.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiSlider(4, width / 2 + 5, calculateHeight(4), 150, 20, "Pressed Green: ", "",
            0, 255, pressedGreen.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                pressedGreen.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiSlider(5, width / 2 + 5, calculateHeight(5), 150, 20, "Pressed Blue: ", "",
            0, 255, pressedBlue.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                pressedBlue.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        buttonList.add(new GuiButton(6, 5, height - 25, 100, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 6) mc.displayGuiScreen(new GuiScreenKeystrokes(mod));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) mc.displayGuiScreen(new GuiScreenKeystrokes(mod));
        else super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mod.getRenderer().renderKeystrokes();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        if (updated) mod.getSettings().save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
