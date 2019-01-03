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

public class GuiScreenSettings extends GuiScreenSidebar {

    private byte byte0;
    private GuiButton buttonToggle;
    private GuiButton buttonNumbers;
    private GuiButton buttonShadow;
    private GuiSlider sliderScale;

    public GuiScreenSettings(final SidebarAddon addon) {
        super(addon);
        this.byte0 = -16;
    }

    public void initGui() {
        this.buttonList.add(this.buttonToggle = new GuiButton(0, this.getCenter() - 75, this.getRowPos(1), 150, 20, "Sidebar: " + this.getSuffix(this.sidebar.enabled)));
        this.buttonList.add(this.buttonNumbers = new GuiButton(1, this.getCenter() - 75, this.getRowPos(2), 150, 20, "Red Numbers: " + this.getSuffix(this.sidebar.redNumbers)));
        this.buttonList.add(this.buttonShadow = new GuiButton(2, this.getCenter() - 75, this.getRowPos(3), 150, 20, "Shadow: " + this.getSuffix(this.sidebar.shadow)));
        this.buttonList.add(new GuiButton(3, this.getCenter() - 75, this.getRowPos(4), 150, 20, "Change Background"));
        this.buttonList.add(this.sliderScale = new GuiSlider(4, this.getCenter() - 75, this.getRowPos(5), 150, 20, "Scale: ", "%", 50.0, 200.0, Math.round(this.sidebar.scale * 100.0f), false, true));
        this.buttonList.add(new GuiButton(5, this.getCenter() - 75, this.getRowPos(6), 150, 20, "Reset Sidebar"));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
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
        if (this.sliderScale.dragging) {
            this.sidebar.scale = this.sliderScale.getValueInt() / 100.0f;
        }
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                this.sidebar.enabled = !this.sidebar.enabled;
                button.displayString = "Sidebar: " + this.getSuffix(this.sidebar.enabled);
                break;
            }
            case 1: {
                this.sidebar.redNumbers = !this.sidebar.redNumbers;
                button.displayString = "Red Numbers: " + this.getSuffix(this.sidebar.redNumbers);
                break;
            }
            case 2: {
                this.sidebar.shadow = !this.sidebar.shadow;
                button.displayString = "Shadow: " + this.getSuffix(this.sidebar.shadow);
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiScreenBackground(this, this.addon));
                break;
            }
            case 5: {
                this.sidebar.scale = 1.0f;
                this.sidebar.color = 0;
                this.sidebar.alpha = 50;
                this.sidebar.chromaEnabled = false;
                this.sidebar.chromaSpeed = 2;
                this.sliderScale.setValue(100.0);
                this.sliderScale.updateSlider();
                final GuiSidebar sidebar = this.sidebar;
                final GuiSidebar sidebar2 = this.sidebar;
                final boolean b = false;
                sidebar2.offsetX = (b ? 1 : 0);
                sidebar.offsetY = (b ? 1 : 0);
                break;
            }
        }
    }

    private String getSuffix(final boolean enabled) {
        return enabled ? (EnumChatFormatting.GREEN + "Enabled") : (EnumChatFormatting.RED + "Disabled");
    }
}
