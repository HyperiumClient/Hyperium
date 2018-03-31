/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.event.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static cc.hyperium.gui.HyperiumGui.clamp;
import static cc.hyperium.gui.HyperiumGui.easeOut;

public class ConfirmationPopup {
    private Queue<Confirmation> confirmations = new LinkedList<>();
    private Confirmation currentConfirmation;

    @InvokeEvent
    public void onFriend(HypixelFriendRequestEvent e) {
        displayConfirmation("Friend request from " + e.getFrom(), accept -> {
            Minecraft.getMinecraft().thePlayer.sendChatMessage((accept ? "/friend accept " : "/friend deny ") + e.getFrom());
            currentConfirmation.framesLeft = 0;
        }, 5);
    }

    @InvokeEvent
    public void onParty(HypixelPartyInviteEvent e) {
        displayConfirmation("Party request from " + e.getFrom(), accept -> {
            if (accept) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/party accept " + e.getFrom());
            }

            currentConfirmation.framesLeft = 0;
        }, 5);
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent e) {
        if (currentConfirmation == null) {
            currentConfirmation = confirmations.poll();
            return;
        }

        if (currentConfirmation.render())
            currentConfirmation = confirmations.poll();
    }

    @InvokeEvent
    public void onKeypress(KeypressEvent e) {
        if (currentConfirmation == null || Minecraft.getMinecraft().currentScreen != null) return;

        if (e.getKey() == Keyboard.KEY_Y) {
            currentConfirmation.callback.accept(true);
        } else if (e.getKey() == Keyboard.KEY_N) {
            currentConfirmation.callback.accept(false);
        }
    }

    public Confirmation displayConfirmation(String text, Consumer<Boolean> callback, int seconds) {
        Confirmation c = new Confirmation(seconds * 60, seconds * 60, text, callback);
        confirmations.add(c);
        return c;
    }


    class Confirmation {
        private long framesLeft;
        private String text;
        private Consumer<Boolean> callback;
        private long upperThreshold;
        private long lowerThreshold;
        private float percentComplete;
        private long systemTime;

        Confirmation(long framesLeft, long frames, String text, Consumer<Boolean> callback) {
            this.framesLeft = framesLeft;
            this.text = text;
            this.callback = callback;

            long fifth = frames / 5;
            upperThreshold = frames - fifth;
            lowerThreshold = fifth;
            this.percentComplete = 0.0f;
            this.systemTime = Minecraft.getSystemTime();
        }

        public boolean render() {
            if (framesLeft <= 0) {
                return true;
            }

            while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                this.framesLeft--;
                this.systemTime += (1000 / 60);
            }

            this.percentComplete = clamp(
                    easeOut(
                            this.percentComplete,
                            this.framesLeft < lowerThreshold ? 0.0f :
                                    this.framesLeft > upperThreshold ? 1.0f : framesLeft,
                            0.01f,
                            15f
                    ),
                    0.0f,
                    1.0f
            );

            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

            int middle = sr.getScaledWidth() / 2;
            int halfWidth = 105;
            int currWidth = (int) (halfWidth * percentComplete);

            // Background
            Gui.drawRect(
                    middle - currWidth,
                    50,
                    middle + currWidth,
                    95,
                    new Color(30, 30, 30).getRGB()
            );

            if (this.percentComplete == 1.0F) {
                long length = upperThreshold - lowerThreshold;
                long current = framesLeft - lowerThreshold;
                float progress = 1.0F - clamp((float) current / (float) length, 0.0F, 1.0F);
                System.out.println("l: " + length + ",c: " + current + ",p: " + progress);

                // Progress
                Gui.drawRect(
                        middle - currWidth,
                        93,
                        (int) (middle - currWidth + (210 * progress)),
                        95,
                        new Color(149, 201, 144).getRGB()
                );

                fr.drawString(text, sr.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, 58, 0xFFFFFF);
                String s = "[Y] Accept [N] Deny";
                fr.drawString(s, sr.getScaledWidth() / 2 - fr.getStringWidth(s) / 2, 70, new Color(170, 170, 170).getRGB());
            }

            return false;
        }
    }

}
