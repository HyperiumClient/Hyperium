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

import com.hcc.event.InvokeEvent;
import com.hcc.event.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class NotificationCenter {

    private String title;
    private String description;

    private Long ticks = 0L;
    private Long endTicks = 0L;

    @InvokeEvent
    public void onRenderTick(RenderEvent event) {
        if (ticks > endTicks) {
            ticks = 0L;
            endTicks = 0L;
            return;
        }
        //System.out.println("Render tick for Notification fired ");

        FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        int x = 0;
        int w = Minecraft.getMinecraft().displayWidth, h = Minecraft.getMinecraft().displayHeight;

        // Background
        Gui.drawRect(((w - fontRendererObj.getStringWidth(description)) - 10), h - 60, w, h - 30, new Color(54, 57, 62, 150).getRGB());
        Gui.drawRect(((w - fontRendererObj.getStringWidth(description)) - 10), h - 60, (w - (fontRendererObj.getStringWidth(description) + 20)) + 3, h - 30, new Color(149, 201, 144).getRGB());
        fontRendererObj.drawString(title, (w - fontRendererObj.getStringWidth(description)) + 5 + x, h - 55, 0xFFFFFF);
        fontRendererObj.drawString(title, (w - fontRendererObj.getStringWidth(description)) + 5 + x, h - 40, 0x424242);
        ticks++;
    }

    public void display(String title, String description, float seconds) {
        System.out.println("Displaying " + title + ":" + description + " for " + seconds + "s");
        this.title = title;
        this.description = description;
        this.endTicks = (long) (seconds * 20);
        System.out.println("t=" + this.ticks);
        System.out.println("et= " + this.endTicks);
    }
}
