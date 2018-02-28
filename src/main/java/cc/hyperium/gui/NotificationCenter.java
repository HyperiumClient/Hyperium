/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.gui;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class NotificationCenter extends Gui {

    private String title;
    private String description;

    private int ticks = 0;
    private int endTicks = 0;

    @InvokeEvent
    public void tick(TickEvent ev) {
        if (this.ticks >= this.endTicks) {
            return;
        }
        ticks++;

    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {

        if (this.ticks >= this.endTicks) {
            return;
        }

        FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;

        int rectW = 175;
        int rectH = 40;
        int x = 0;

        //Sk1er test changes to make it look better
        // Goal: Alpha blend it out during first/last 20% of movement + make it move better

        float percentComplete = ticks / 20F;
        float alpha = 255;

        // animation
        if (ticks < 20) {
            x = (rectW / 20) * (20 - ticks) * (20 - ticks) / 2;
        } else if ((endTicks - ticks) < 20) {
            x = (rectW / 20) * (20 - (endTicks - ticks)) * (20 - (endTicks - ticks))/2;
        }
        if (x > rectW / 4) {
            float e = rectW / 4F;
            float t = x - rectW;
            alpha = e / t;
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int w = sr.getScaledWidth();
        int h = sr.getScaledHeight();
        if (alpha <= 4)
            alpha = 10;

        // Background
        Gui.drawRect(w - rectW + x, (h - 30) - rectH, w + x, h - 30, new Color(30, 30, 30, (int) alpha).getRGB());
        Gui.drawRect(
                w - rectW + x,
                (h - 30) - rectH,
                (w - rectW) + 3 + x,
                h - 30,
                new Color(149, 201, 144).getRGB());
        fontRendererObj.drawString(title, (w - rectW) + 10 + x, (h - 30) - rectH + 10, 0xFFFFFF);
        fontRendererObj.drawString(description, (w - rectW) + 10 + x, (h - 30) - rectH + 20, 0x424242);

    }

    public void display(String title, String description, float seconds) {
        this.title = title;
        this.description = description;
        this.ticks = 0;
        this.endTicks = (int) (seconds * 20);
    }
}
