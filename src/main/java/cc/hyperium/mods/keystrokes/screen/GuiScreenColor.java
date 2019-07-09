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

package cc.hyperium.mods.keystrokes.screen;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiScreenColor extends GuiScreen {

    private final KeystrokesMod mod; // OOP

    private final IScrollable scrollable1; // Red
    private final IScrollable scrollable2; // Green
    private final IScrollable scrollable3; // Blue
    private final IScrollable scrollable4; // Gamma

    private boolean updated = false; // Have the values been changed?

    GuiScreenColor(KeystrokesMod mod, IScrollable scrollable1, IScrollable scrollable2, IScrollable scrollable3) {
        this.mod = mod;

        this.scrollable1 = scrollable1;
        this.scrollable2 = scrollable2;
        this.scrollable3 = scrollable3;
        this.scrollable4 = null;
    }

    GuiScreenColor(KeystrokesMod mod, IScrollable scrollable1, IScrollable scrollable2, IScrollable scrollable3, IScrollable scrollable4) {
        this.mod = mod;

        this.scrollable1 = scrollable1;
        this.scrollable2 = scrollable2;
        this.scrollable3 = scrollable3;
        this.scrollable4 = scrollable4;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiSlider(0, this.width / 2 - 100, this.height / 2 - 32, 200, 20, "Red: ", "", 0, 255, this.scrollable1.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable1.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        this.buttonList.add(new GuiSlider(1, this.width / 2 - 100, this.height / 2 - 10, 200, 20, "Green: ", "", 0, 255, this.scrollable2.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable2.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        this.buttonList.add(new GuiSlider(2, this.width / 2 - 100, this.height / 2 + 12, 200, 20, "Blue: ", "", 0, 255, this.scrollable3.getAmount(), false, true) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                scrollable3.onScroll(getValue(), getValueInt());
                updated = true;
            }
        });
        if (this.scrollable4 != null) {
            this.buttonList.add(new GuiSlider(3, this.width / 2 - 100, this.height / 2 + 34, 200, 20, "Alpha: ", "", 0, 255, this.scrollable4.getAmount(), false, true) {
                @Override
                public void updateSlider() {
                    super.updateSlider();
                    scrollable4.onScroll(getValue(), getValueInt());
                    updated = true;
                }
            });
        }
        this.buttonList.add(new GuiButton(4, 5, this.height - 25, 100, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 4) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiScreenKeystrokes(this.mod));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiScreenKeystrokes(this.mod));
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mod.getRenderer().renderKeystrokes();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        if (this.updated) {
            this.mod.getSettings().save();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
