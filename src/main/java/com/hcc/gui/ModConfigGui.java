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

package com.hcc.gui;

import com.hcc.gui.font.Fonts;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class ModConfigGui extends GuiScreen {

    @Override
    public void initGui() {
        buttonList.add(new CustomFontButton(0, getX(0), getY(), 50, 25, "Home").setFontRenderer(Fonts.MONTSERRAT_LIGHT.get()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        //background
        drawRect(width / 5, height /5, width - width/5, height - height/5, new Color(0, 0, 0, 100).getRGB());
        //top menu
        drawRect(width - width / 5, height /5, width / 5, height /5 + 25, new Color(0, 0, 0, 100).getRGB());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    private int getX(int n){
        return (width / 5) + (50 * n);
    }

    private int getY(){
        return height /5;
    }

}
