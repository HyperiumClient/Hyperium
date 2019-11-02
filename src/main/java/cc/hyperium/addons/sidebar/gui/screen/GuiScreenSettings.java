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
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiScreenSettings extends GuiScreenSidebar implements GuiScreenHelper {

    private byte byte0;
    private GuiSlider sliderScale;

    public GuiScreenSettings(SidebarAddon addon) {
        super(addon);
        byte0 = -16;
    }

    public void initGui() {
        buttonList.add(new GuiButton(0, getCenter() - 75, getRowPos(1), 150, 20, "Sidebar: " + getSuffix(sidebar.enabled)));
        buttonList.add(new GuiButton(1, getCenter() - 75, getRowPos(2), 150, 20, "Red Numbers: " + getSuffix(sidebar.redNumbers)));
        buttonList.add(new GuiButton(2, getCenter() - 75, getRowPos(3), 150, 20, "Shadow: " + getSuffix(sidebar.shadow)));
        buttonList.add(new GuiButton(3, getCenter() - 75, getRowPos(4), 150, 20, "Change Background"));
        buttonList.add(sliderScale = new GuiSlider(4, getCenter() - 75, getRowPos(5), 150, 20, "Scale: ", "%", 50.0, 200.0, Math.round(sidebar.scale * 100.0f), false, true));
        buttonList.add(new GuiButton(5, getCenter() - 75, getRowPos(6), 150, 20, "Reset Sidebar"));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Hyperium.CONFIG.save();
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
        if (sliderScale.dragging) sidebar.scale = sliderScale.getValueInt() / 100.0f;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                sidebar.enabled = !sidebar.enabled;
                button.displayString = "Sidebar: " + getSuffix(sidebar.enabled);
                break;

            case 1:
                sidebar.redNumbers = !sidebar.redNumbers;
                button.displayString = "Red Numbers: " + getSuffix(sidebar.redNumbers);
                break;

            case 2:
                sidebar.shadow = !sidebar.shadow;
                button.displayString = "Shadow: " + getSuffix(sidebar.shadow);
                break;

            case 3:
                mc.displayGuiScreen(new GuiScreenBackground(this, addon));
                break;

            case 5:
                sidebar.scale = 1.0f;
                sidebar.color = 0;
                sidebar.alpha = 50;
                sidebar.chromaEnabled = false;
                sidebar.chromaSpeed = 2;
                sliderScale.setValue(100.0);
                sliderScale.updateSlider();
                sidebar.offsetX = 0;
                sidebar.offsetY = 0;
                break;
        }
    }
}
