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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class NotificationCenter {
    private String title;
    private String description;
    private Long ticks = 0L;
    private Long startTick = 0L;
    public void onTick(){
        FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        if(ticks <= 0) return;
        int x =0; // position changes
//        if(startTick - ticks < 20)
//            x+=(startTick - ticks)*3;
        int w = Minecraft.getMinecraft().displayWidth, h = Minecraft.getMinecraft().displayHeight;

        // Background
        Gui.drawRect((w - (fontRendererObj.getStringWidth(description)+20))+x, h - 60, w, h- 30, new Color(0, 0, 0, 100).getRGB());
        Gui.drawRect((w - (fontRendererObj.getStringWidth(description)+20))+x, h - 60, (w - (fontRendererObj.getStringWidth(description)+20))+3, h- 30, new Color(149, 201, 144).getRGB());

        ticks--;
    }
    public void display(String title, String description, Long ticks){
        this.title = title;
        this.description = description;
        this.startTick = (this.ticks = ticks);
    }
}
