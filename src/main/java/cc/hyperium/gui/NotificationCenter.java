/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import static cc.hyperium.gui.HyperiumGui.clamp;
import static cc.hyperium.gui.HyperiumGui.easeOut;

public class NotificationCenter extends Gui {
    private Queue<Notification> notifications = new LinkedList<>();
    private Notification currentNotification;
    private HyperiumFontRenderer fontRenderer = new HyperiumFontRenderer("Arial", Font.PLAIN, 18);
    private HashMap<Integer, Boolean> mouseState;
    private HashMap<Integer, Float[]> draggedState;

    public NotificationCenter() {
        this.mouseState = new HashMap<>(5);
        for (int i = 0; i < 5; i++)
            this.mouseState.put(i, false);
        draggedState = new HashMap<>();
    }

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
        handleMouseInput();

        if (currentNotification != null) {
            currentNotification.render();
        }
    }

    public Notification display(String title, String description, float seconds) {
        Notification notif = new Notification(title, description, (int) (seconds * 20));

        try {
            notifications.add(notif);
            return notif;
        } catch (Exception e) {
            Hyperium.LOGGER.error("Can't display notification!", e);
        }

        return null;
    }

    private void handleMouseInput() {
        if (!Mouse.isCreated()) return;

        for (int button = 0; button < 5; button++) {
            handleDragged(button);

            // normal click
            if (Mouse.isButtonDown(button) == this.mouseState.get(button)) continue;

            if (currentNotification != null) {
                currentNotification.onClick(getMouseX(), getMouseY(), button, Mouse.isButtonDown(button));
            }

            this.mouseState.put(button, Mouse.isButtonDown(button));

            // add new dragged
            if (Mouse.isButtonDown(button))
                this.draggedState.put(button, new Float[]{ getMouseX(), getMouseY() });

            // remove old dragged
            if (Mouse.isButtonDown(button)) continue;
            if (!this.draggedState.containsKey(button)) continue;
            this.draggedState.remove(button);
        }
    }

    private void handleDragged(int button) {
        if (!this.draggedState.containsKey(button))
            return;

        if (currentNotification != null) {
            currentNotification.onDrag(
                    getMouseX() - this.draggedState.get(button)[0],
                    getMouseY() - this.draggedState.get(button)[1],
                    getMouseX(),
                    getMouseY(),
                    button
            );
        }

        // update dragged
        this.draggedState.put(button, new Float[]{ getMouseX(), getMouseY() });
    }

    private float getMouseY() {
        float my = (float) Mouse.getY();
        float rh = (float) new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
        float dh = (float) Minecraft.getMinecraft().displayHeight;
        return rh - my * rh / dh - 1L;
    }

    private float getMouseX() {
        float mx = (float) Mouse.getX();
        float rw = (float) new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        float dw = (float) Minecraft.getMinecraft().displayWidth;
        return mx * rw / dw;
    }



    public class Notification {
        private String title;
        private String description;
        private int ticksLeft;
        private float percentComplete;
        private int topThreshhold;
        private int lowerThreshhold;
        private boolean dragging = false;
        private Runnable clickedCallback;

        public Notification(String title, String description, int ticksLeft) {
            this.title = title;
            this.description = description;
            this.ticksLeft = ticksLeft;

            int fifth = ticksLeft / 5;
            this.topThreshhold = ticksLeft - fifth;
            this.lowerThreshhold = fifth;
            this.percentComplete = 0.0F;

            this.clickedCallback = () -> {};
        }

        public void setClickedCallback(Runnable runnable) {
            this.clickedCallback = runnable;
        }

        public boolean tick() {
            this.ticksLeft--;

            return ticksLeft <= 0;
        }

        public void onClick(float x, float y, int button, boolean down) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

            int width = 175;
            ArrayList<String> lines = fontRenderer.splitString(description, width - 10);
            int height = (int) (30 + fontRenderer.getHeight(String.join("\n\r", lines)));

            int notifX = (int) (sr.getScaledWidth() - (width * this.percentComplete));
            int notifY = sr.getScaledHeight() - height - 15;

            if (x > notifX && x < notifX + width
                    && y > notifY && y < notifY + height
                    && button == 0
                    && !down
                    ) {
                clickedCallback.run();
            }
        }

        public void onDrag(float dx, float dy, float x, float y, int button) {

        }

        public void render() {
            if (ticksLeft <= 0) {
                return;
            }

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
            ArrayList<String> lines = fontRenderer.splitString(description, width - 10);
            int height = (int) (30 + fontRenderer.getHeight(String.join("\n\r", lines)));

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
            fontRenderer.drawString(
                    title,
                    x + 10,
                    y + 5,
                    0xFFFFFF
            );

            // Notification Body
            fontRenderer.drawSplitString(
                    lines,
                    x + 10,
                    (int) (y + 5 + fontRenderer.getHeight(title) + 2),
                    0x424242
            );
        }
    }
}
