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

package cc.hyperium.addons.sidebar.gui.screen;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiScreenBackground extends GuiScreenSidebar implements GuiScreenHelper {

    private byte byte0;
    private GuiScreen parent;
    private GuiButton buttonDone;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;
    private GuiSlider sliderChromaSpeed;
    private GuiButton buttonChromaType;

    GuiScreenBackground(GuiScreen parent, SidebarAddon addon) {
        super(addon);
        byte0 = -16;
        this.parent = parent;
    }

    public void initGui() {
        buttonList.add(new GuiButton(0, getCenter() - 75, getRowPos(1), 150, 20, "Chroma: " + getSuffix(sidebar.chromaEnabled)));
        buttonList.add(buttonChromaType = new GuiButton(7, getCenter() - 75, getRowPos(2), 150, 20, "Chroma type: " + sidebar.chromaType.getName()));
        buttonList.add(sliderRed = new GuiSlider(1, getCenter() - 75, getRowPos(2), 150, 20, "Red: ", "", 0.0, 255.0, sidebar.color >> 16 & 0xFF, false, true));
        buttonList.add(sliderGreen = new GuiSlider(2, getCenter() - 75, getRowPos(3), 150, 20, "Green: ", "", 0.0, 255.0, sidebar.color >> 8 & 0xFF, false, true));
        buttonList.add(sliderBlue = new GuiSlider(3, getCenter() - 75, getRowPos(4), 150, 20, "Blue: ", "", 0.0, 255.0, sidebar.color & 0xFF, false, true));
        buttonList.add(sliderAlpha = new GuiSlider(4, getCenter() - 75, getRowPos(5), 150, 20, "Alpha: ", "", 0.0, 255.0, sidebar.alpha, false, true));
        buttonList.add(sliderChromaSpeed = new GuiSlider(3, getCenter() - 75, getRowPos(4), 150, 20, "Chroma Speed: ", "", 1.0, 10.0, sidebar.chromaSpeed, false, true));
        buttonList.add(buttonDone = new GuiButton(6, getCenter() - 75, getRowPos(6), 150, 20, "Done"));
        setSlidersVisibility();
    }

    private int getRowPos(int rowNumber) {
        return height / 4 + (24 * rowNumber - 24) + byte0;
    }

    public int getCenter() {
        return width / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        updateSettings();
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                sidebar.chromaEnabled = !sidebar.chromaEnabled;
                button.displayString = "Chroma: " + getSuffix(sidebar.chromaEnabled);
                setSlidersVisibility();
                break;

            case 6:
                mc.displayGuiScreen(parent);
                break;

            case 7:
                sidebar.chromaType = GuiSidebar.ChromaType.next(sidebar.chromaType);
                buttonChromaType.displayString = "Chroma type: " + sidebar.chromaType.getName();
                break;
        }
    }

    private void setSlidersVisibility() {
        boolean mode = sidebar.chromaEnabled;
        sliderRed.visible = !mode;
        sliderGreen.visible = !mode;
        sliderBlue.visible = !mode;
        sliderChromaSpeed.visible = mode;
        buttonChromaType.visible = mode;

        sliderAlpha.yPosition = (mode ? getRowPos(3) : getRowPos(5));
        buttonDone.yPosition = (mode ? getRowPos(5) : getRowPos(6));
    }

    private void updateSettings() {
        sidebar.color = (sliderRed.getValueInt() << 16 | sliderGreen.getValueInt() << 8 | sliderBlue.getValueInt());
        sidebar.alpha = sliderAlpha.getValueInt();
        sidebar.chromaSpeed = sliderChromaSpeed.getValueInt();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Hyperium.CONFIG.save();
    }
}
