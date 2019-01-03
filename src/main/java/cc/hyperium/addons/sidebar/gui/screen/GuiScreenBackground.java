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

package cc.hyperium.addons.sidebar.gui.screen;

import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiScreenBackground extends GuiScreenSidebar {

    private byte byte0;
    private GuiScreen parent;
    private GuiButton buttonDone;
    private GuiSlider sliderRed;
    private GuiSlider sliderGreen;
    private GuiSlider sliderBlue;
    private GuiSlider sliderAlpha;
    private GuiSlider sliderChromaSpeed;
    private GuiButton buttonChromaType;

    public GuiScreenBackground(final GuiScreen parent, final SidebarAddon addon) {
        super(addon);
        this.byte0 = -16;
        this.parent = parent;
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.getCenter() - 75, this.getRowPos(1), 150, 20, "Chroma: " + this.getSuffix(this.sidebar.chromaEnabled)));
        this.buttonList.add(this.buttonChromaType = new GuiButton(7, this.getCenter() - 75, this.getRowPos(2), 150, 20, "Chroma type: " + this.sidebar.chromaType.getName()));
        this.buttonList.add(this.sliderRed = new GuiSlider(1, this.getCenter() - 75, this.getRowPos(2), 150, 20, "Red: ", "", 0.0, 255.0, this.sidebar.color >> 16 & 0xFF, false, true));
        this.buttonList.add(this.sliderGreen = new GuiSlider(2, this.getCenter() - 75, this.getRowPos(3), 150, 20, "Green: ", "", 0.0, 255.0, this.sidebar.color >> 8 & 0xFF, false, true));
        this.buttonList.add(this.sliderBlue = new GuiSlider(3, this.getCenter() - 75, this.getRowPos(4), 150, 20, "Blue: ", "", 0.0, 255.0, this.sidebar.color & 0xFF, false, true));
        this.buttonList.add(this.sliderAlpha = new GuiSlider(4, this.getCenter() - 75, this.getRowPos(5), 150, 20, "Alpha: ", "", 0.0, 255.0, this.sidebar.alpha, false, true));
        this.buttonList.add(this.sliderChromaSpeed = new GuiSlider(3, this.getCenter() - 75, this.getRowPos(4), 150, 20, "Chroma Speed: ", "", 1.0, 10.0, this.sidebar.chromaSpeed, false, true));
        this.buttonList.add(this.buttonDone = new GuiButton(6, this.getCenter() - 75, this.getRowPos(6), 150, 20, "Done"));
        this.setSlidersVisibility();
    }

    public int getRowPos(final int rowNumber) {
        return this.height / 4 + (24 * rowNumber - 24) + this.byte0;
    }

    public int getCenter() {
        return this.width / 2;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.updateSettings();
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.sidebar.chromaEnabled = !this.sidebar.chromaEnabled;
                button.displayString = "Chroma: " + this.getSuffix(this.sidebar.chromaEnabled);
                this.setSlidersVisibility();
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 7:
                this.sidebar.chromaType = GuiSidebar.ChromaType.next(this.sidebar.chromaType);
                buttonChromaType.displayString = "Chroma type: " + this.sidebar.chromaType.getName();
                break;
        }
    }

    private void setSlidersVisibility() {
        final boolean mode = this.sidebar.chromaEnabled;
        this.sliderRed.visible = !mode;
        this.sliderGreen.visible = !mode;
        this.sliderBlue.visible = !mode;
        this.sliderChromaSpeed.visible = mode;
        this.buttonChromaType.visible = mode;

        this.sliderAlpha.yPosition = (mode ? this.getRowPos(3) : this.getRowPos(5));
        this.buttonDone.yPosition = (mode ? this.getRowPos(5) : this.getRowPos(6));
    }

    private void updateSettings() {
        this.sidebar.color = (this.sliderRed.getValueInt() << 16 | this.sliderGreen.getValueInt() << 8 | this.sliderBlue.getValueInt());
        this.sidebar.alpha = this.sliderAlpha.getValueInt();
        this.sidebar.chromaSpeed = this.sliderChromaSpeed.getValueInt();
    }

    private String getSuffix(final boolean enabled) {
        return enabled ? (EnumChatFormatting.GREEN + "Enabled") : (EnumChatFormatting.RED + "Disabled");
    }
}
