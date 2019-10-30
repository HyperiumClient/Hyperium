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

package cc.hyperium.integrations.watchdog;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.handlers.handlers.HypixelDetector;
import net.minecraft.client.Minecraft;

public class ThankWatchdog {
    private static final String WATCHDOG_BAN_TRIGGER = "A player has been removed from your game for hacking or abuse. Thanks for reporting it!";
    private static final String WATCHDOG_ANNOUNCEMENT_TRIGGER = "[WATCHDOG ANNOUNCEMENT]";
    private static final String THANK_WATCHDOG_MESSAGE = "/achat Thanks Watchdog!";

    @InvokeEvent
    public void onChat(ChatEvent e) {
        if ((e.getChat().getUnformattedText().contains(WATCHDOG_BAN_TRIGGER) || e.getChat().getUnformattedText().contains(WATCHDOG_ANNOUNCEMENT_TRIGGER)) &&
            Settings.THANK_WATCHDOG && HypixelDetector.getInstance().isHypixel()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(THANK_WATCHDOG_MESSAGE);
        }
    }
}

