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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;

import java.io.IOException;

public class GuiScreenSidebar extends GuiScreen {

    protected SidebarAddon addon;
    private boolean dragging;
    private int lastX;
    private int lastY;
    GuiSidebar sidebar;

    GuiScreenSidebar(SidebarAddon addon) {
        this.addon = addon;
        sidebar = addon.getSidebarGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (mc.thePlayer != null) {
            ScoreObjective scoreObjective = mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreObjective != null) sidebar.drawSidebar(scoreObjective, new ScaledResolution(mc));
        }

        if (dragging) {
            sidebar.offsetX += mouseX - lastX;
            sidebar.offsetY += mouseY - lastY;
        }

        lastX = mouseX;
        lastY = mouseY;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (sidebar.contains(mouseX, mouseY)) dragging = true;
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    public void onGuiClosed() {
        Hyperium.CONFIG.save();
    }
}
