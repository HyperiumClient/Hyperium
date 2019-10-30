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
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.awt.*;

public class GuiHyperiumScreenTos extends GuiScreen {

    private static boolean firstAccept;

    public final int BUTTON_ID_ACCEPT = 0;

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(BUTTON_ID_ACCEPT, (width / 2) - 100, 160,
            firstAccept ? ChatFormatting.GREEN + I18n.format("button.disclaimer.confirm") : I18n.format("button.disclaimer.accept")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawRect(0, 79, width, 148, 0x66000000);
        drawRect(0, 80, width, 149, 0x66000000);

        drawCenteredString(fontRendererObj, I18n.format("disclaimer.line1",
            ChatFormatting.BOLD + I18n.format("disclaimer.line1.bold") + ChatFormatting.RESET),
            width / 2, 90, Color.WHITE.getRGB());

        drawCenteredString(fontRendererObj, I18n.format("disclaimer.line2"), width / 2, 100,
            Color.WHITE.getRGB());

        drawCenteredString(fontRendererObj, I18n.format("disclaimer.line3"), width / 2, 110,
            Color.WHITE.getRGB());

        drawCenteredString(fontRendererObj, I18n.format("disclaimer.line4",
            ChatFormatting.GRAY + I18n.format("disclaimer.line4.policylink")
                + ChatFormatting.RESET), width / 2, 120, Color.WHITE.getRGB());

        drawCenteredString(fontRendererObj, I18n.format("disclaimer.line5"), width / 2, 130,
            Color.WHITE.getRGB());
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == BUTTON_ID_ACCEPT) {
            if (!firstAccept) {
                firstAccept = true;
                mc.displayGuiScreen(new GuiHyperiumScreenTos());
            } else {
                Hyperium.INSTANCE.acceptTos();
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenMainMenu());
            }
        }
    }
}
