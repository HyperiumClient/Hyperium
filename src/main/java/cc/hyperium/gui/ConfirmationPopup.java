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

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.interact.KeyPressEvent;
import cc.hyperium.event.network.server.hypixel.HypixelFriendRequestEvent;
import cc.hyperium.event.network.server.hypixel.HypixelPartyInviteEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class ConfirmationPopup {

    private final Queue<Confirmation> confirmations = new LinkedList<>();
    private Confirmation currentConfirmation;
    private String acceptFrom = "";

    @InvokeEvent
    public void onFriend(HypixelFriendRequestEvent e) {
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            displayConfirmation("Friend request from " + e.getFrom(), accept -> {
                Minecraft.getMinecraft().thePlayer.sendChatMessage((accept ? "/friend accept " : "/friend deny ") + e.getFrom());
                currentConfirmation.framesLeft = 0;
            });
        }
    }

    @InvokeEvent
    public void onParty(HypixelPartyInviteEvent e) {
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) {
            displayConfirmation("Party request from " + e.getFrom(), accept -> {
                if (accept) Minecraft.getMinecraft().thePlayer.sendChatMessage("/party accept " + e.getFrom());
                currentConfirmation.framesLeft = 0;
            });
        }
    }

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent e) {
        if (currentConfirmation == null) {
            currentConfirmation = confirmations.poll();
            return;
        }

        if (currentConfirmation.render()) currentConfirmation = confirmations.poll();
    }

    @InvokeEvent
    public void onKeypress(KeyPressEvent e) {
        if (currentConfirmation == null || Minecraft.getMinecraft().currentScreen != null) return;

        if (e.getKey() == Keyboard.KEY_Y) currentConfirmation.callback.accept(true);
        else if (e.getKey() == Keyboard.KEY_N) currentConfirmation.callback.accept(false);
    }

    private void displayConfirmation(String text, Consumer<Boolean> callback) {
        Confirmation c = new Confirmation(5 * 60, 5 * 60, text, callback);
        if (Settings.SHOW_INGAME_CONFIRMATION_POPUP) confirmations.add(c);
    }

    public void setAcceptFrom(String acceptFrom) {
        this.acceptFrom = acceptFrom;
    }

    public class Confirmation {
        private final String text;
        private final Consumer<Boolean> callback;
        private final long upperThreshold;
        private final long lowerThreshold;
        private long framesLeft;
        private float percentComplete;
        private long systemTime;

        Confirmation(long framesLeft, long frames, String text, Consumer<Boolean> callback) {
            this.framesLeft = framesLeft;
            this.text = text;
            this.callback = callback;
            long fifth = frames / 5;

            upperThreshold = frames - fifth;
            lowerThreshold = fifth;
            percentComplete = 0.0f;
            systemTime = Minecraft.getSystemTime();
        }

        public boolean render() {
            if (framesLeft <= 0) return true;

            if (text.equalsIgnoreCase("Party request from " + acceptFrom)) {
                callback.accept(true);
                return true;
            }

            while (systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                framesLeft--;
                systemTime += (1000 / 60);
            }

            percentComplete = HyperiumGui.clamp(
                HyperiumGui.easeOut(
                    percentComplete,
                    framesLeft < lowerThreshold ? 0.0f :
                        framesLeft > upperThreshold ? 1.0f : framesLeft,
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
                new Color(27, 27, 27).getRGB()
            );

            if (percentComplete == 1.0F) {
                long length = upperThreshold - lowerThreshold;
                long current = framesLeft - lowerThreshold;
                float progress = 1.0F - HyperiumGui.clamp((float) current / (float) length, 0.0F, 1.0F);

                // Progress
                Gui.drawRect(
                    middle - currWidth,
                    93,
                    (int) (middle - currWidth + (210 * progress)),
                    95,
                    new Color(128, 226, 126).getRGB()
                );

                fr.drawString(text, sr.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, 58, 0xFFFFFF);

                String s = ChatColor.GREEN + "[Y] Accept " + ChatColor.RED + "[N] Deny";
                fr.drawString(s, sr.getScaledWidth() / 2 - fr.getStringWidth(s) / 2, 70, -1);
            }

            return false;
        }
    }

}
