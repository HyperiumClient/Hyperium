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

package me.semx11.autotip.event;

import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.command.LimboCommand;
import me.semx11.autotip.misc.TipTracker;
import me.semx11.autotip.misc.Writer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

    private final Pattern tippedPattern = Pattern.compile("You tipped (?<tips>\\d+) player.*");
    private final Pattern coinPattern = Pattern.compile(".+\\+(?<coins>\\d+) (?<game>.+) Coin.*");

    @InvokeEvent
    public void onChat(ChatEvent event) {

        if (!Autotip.onHypixel) {
            return;
        }

        String msg = GeneralChatHandler.strip(event.getChat());

        if (Autotip.toggle) {
            if (msg.equals("Slow down! You can only use /tip every few seconds.")
                    || msg.equals("/limbo for more information.")
                    || msg.equals("Still processing your most recent request!")
                    || msg.equals("You are not allowed to use commands as a spectator!")
                    || msg.equals("You cannot give yourself tips!")
                    || msg.startsWith("You already tipped everyone that has boosters active")
                    || msg.startsWith("You can only use the /tip command")
                    || msg.startsWith("You can't tip the same person")
                    || msg.startsWith("You've already tipped someone in the past hour in ")
                    || msg.startsWith("You've already tipped that person")
                    || msg.startsWith("That player is not online, try another user!")) {
                event.setCancelled(true);
            }

            Matcher tippedMatcher = this.tippedPattern.matcher(msg);
            if (tippedMatcher.matches()) {
                int tips = Integer.parseInt(tippedMatcher.group("tips"));
                TipTracker.tipsSent += tips;

                String[] rewards = event.getChat().getChatStyle().getChatHoverEvent().getValue().getFormattedText().split("\n");

                for (int i = 2; i < rewards.length; i++) {
                    Matcher coinMatcher = this.coinPattern.matcher(rewards[i]);

                    if (coinMatcher.matches()) {
                        int coins = Integer.parseInt(coinMatcher.group("coins"));
                        String game = coinMatcher.group("game");
                        TipTracker.tipsSentEarnings.merge(game, coins, (a, b) -> a + b);
                    }
                }

                Writer.execute();

                return;
            }
        }

        if (LimboCommand.executed) {
            if (msg.equals("A kick occurred in your connection, so you have been routed to limbo!")
                    || msg.equals("Illegal characters in chat")) {
                event.setCancelled(true);
            } else if (msg.equals("You were spawned in Limbo.")) {
                event.setCancelled(true);
                LimboCommand.executed = false;
            }
        }
    }
}
