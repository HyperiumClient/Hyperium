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

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiConfirmDisconnect extends GuiScreen {

    private int i = -16;

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + i, 98, 20, I18n.format("menu.disconnect")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height / 4 + 96 + i, 98, 20, I18n.format("gui.cancel")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        fontRendererObj.drawStringWithShadow(I18n.format("gui.confirmdisconnect.text"), (width >> 1) - 90, (height >> 2) + 72 + i, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                boolean integratedServerRunning = mc.isIntegratedServerRunning();
                button.enabled = false;
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(integratedServerRunning ? new GuiMainMenu() : new GuiMultiplayer(new GuiMainMenu()));
                break;
            case 1:
                mc.displayGuiScreen(null);
                break;
        }
    }
}
