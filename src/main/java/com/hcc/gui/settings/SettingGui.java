/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.gui.settings;

import com.hcc.gui.HCCGui;
import com.hcc.gui.font.Fonts;
import com.hcc.utils.HCCFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class SettingGui extends HCCGui {
    private HCCFontRenderer fontRendererObj = Fonts.ARIAL.getTrueTypeFont();
    private String name;
    private GuiScreen previous;
    public SettingGui(String name, GuiScreen previous){
        this.name = name;
        this.previous = previous;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        // The Box
        drawRect(width / 5, height / 5, width - width / 5, height - height / 5, new Color(0, 0, 0, 100).getRGB());
        // Top bar
        drawRect(width / 5, height / 5, width -width/5, height / 5  + 25, new Color(0, 0, 0, 30).getRGB());
        fontRendererObj.drawString(name, width / 5 + 10, (height / 5) + ((35 - 9) / 2), 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void pack() {
        reg(">", new GuiButton(0, (width - width / 5)- 25, height /5, 25, 25, ">"), b -> Minecraft.getMinecraft().displayGuiScreen(previous), b->{});
    }
}
