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

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import static cc.hyperium.gui.HyperiumGui.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class NotificationCenter extends Gui {
    private Queue<Notification> notifications = new LinkedList<>();
    private Notification currentNotification;

    @InvokeEvent
    public void tick(TickEvent ev) {
        if (currentNotification == null) {
            currentNotification = notifications.poll();

            return;
        }

        boolean finished = currentNotification.tick();

        if (finished) {
            currentNotification = notifications.poll();
        }
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent event) {
        if (currentNotification != null) {
            currentNotification.render();
        }
    }

    public void display(String title, String description, float seconds) {
        Notification notif = new Notification(title, description, (int) (seconds * 20));

        try {
            notifications.add(notif);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Can't display notification!", e);
        }
    }

    class Notification {
        private String title;
        private String description;
        private int ticksLeft;
        private float percentComplete;
        private int topThreshhold;
        private int lowerThreshhold;
        private int numLines = 1;

        public Notification(String title, String description, int ticksLeft) {
            this.title = title;
            this.description = description;
            this.ticksLeft = ticksLeft;

            int fifth = ticksLeft / 5;
            this.topThreshhold = ticksLeft - fifth;
            this.lowerThreshhold = fifth;
            this.percentComplete = 0.0F;

            this.numLines = Minecraft.getMinecraft()
                    .fontRendererObj
                    .listFormattedStringToWidth(description, 175 - 15)
                    .size();
        }

        public boolean tick() {
            this.ticksLeft--;

            return ticksLeft <= 0;
        }

        public void render() {
            if (ticksLeft <= 0) {
                return;
            }

            FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;

            this.percentComplete = clamp(
                    easeOut(
                            this.percentComplete,
                            this.ticksLeft < lowerThreshhold ? 0.0f :
                                    this.ticksLeft > topThreshhold ? 1.0f : ticksLeft,
                            0.01f,
                            5f
                    ),
                    0.0f,
                    1.0f
            );

            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

            int width = 175;
            int height = 40 + ((numLines - 1) * fontRendererObj.FONT_HEIGHT);

            int x = (int) (sr.getScaledWidth() - (width * this.percentComplete));
            int y = sr.getScaledHeight() - height - 15;
            float alpha = 255 /* clamp(this.percentComplete, 0.5f, 1.0f)*/;

            // Background
            Gui.drawRect(
                    x,
                    y,
                    x + width,
                    y + height,
                    new Color(30, 30, 30, (int) alpha).getRGB()
            );

            // Highlight color
            Gui.drawRect(
                    x,
                    y,
                    x + 5,
                    y + height,
                    new Color(149, 201, 144).getRGB()
            );

            // Title Text
            fontRendererObj.drawString(
                    title,
                    x + 10,
                    y + 10,
                    0xFFFFFF
            );

            // Notification Body
            fontRendererObj.drawSplitString(
                    description,
                    x + 10,
                    y + 10 + fontRendererObj.FONT_HEIGHT + 2,
                    width - 15,
                    0x424242
            );
        }
    }
}
