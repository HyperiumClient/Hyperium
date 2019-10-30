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

package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.gui.items.CCGuiItem;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class CustomCrosshairScreen extends GuiScreen {

    public List<CCGuiItem> itemList = new ArrayList<>();
    public List<String> toolTip = new ArrayList<>();

    protected CustomCrosshairAddon addon;

    public CustomCrosshairScreen(CustomCrosshairAddon addon) {
        this.addon = addon;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (toolTip != null) {
            CustomCrosshairGraphics
                .drawBorderedRectangle(mouseX + 5, mouseY + 5, mouseX + getMaxWidth() + 9, mouseY + toolTip.size() * 11 + 8, CustomCrosshairAddon.PRIMARY, CustomCrosshairAddon.SECONDARY);
            for (int i = 0; i < toolTip.size(); ++i) {
                CustomCrosshairGraphics
                    .drawString(toolTip.get(i), mouseX + 8, mouseY + i * 11 + 9, 16777215);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        addon.getConfig().saveCurrentCrosshair();
    }

    private int getMaxWidth() {
        int max = 0;
        for (String aToolTip : toolTip) {
            final int currentWidth = fontRendererObj.getStringWidth(aToolTip);
            if (currentWidth > max) {
                max = currentWidth;
            }
        }
        return max;
    }
}

