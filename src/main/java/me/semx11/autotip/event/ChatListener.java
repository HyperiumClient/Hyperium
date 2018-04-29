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
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.command.LimboCommand;
import me.semx11.autotip.misc.TipTracker;
import me.semx11.autotip.misc.Writer;
import me.semx11.autotip.util.ClientMessage;
import me.semx11.autotip.util.MessageOption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

    private Pattern xpPattern = Pattern.compile("\\+50 experience \\(Gave a player a /tip\\)");
    private Pattern playerPattern = Pattern.compile("You tipped (?<player>\\w+) in .*");
    private Pattern coinPattern = Pattern.compile(
            "\\+(?<coins>\\d+) coins for you in (?<game>.+) for being generous :\\)");
    private Pattern earnedPattern = Pattern.compile(
            "You earned (?<coins>\\d+) coins and (?<xp>\\d+) experience from (?<game>.+) tips in the last minute!");

    @InvokeEvent
    public void onChat(ChatEvent event) {

        if (!Autotip.onHypixel) {
            return;
        }

        String msg = GeneralChatHandler.strip(event.getChat());
        MessageOption mOption = Autotip.messageOption;

        if (Autotip.toggle) {
            if (msg.equals("Slow down! You can only use /tip every few seconds.")
                    || msg.equals("Still processing your most recent request!")
                    || msg.equals("You are not allowed to use commands as a spectator!")
                    || msg.equals("You cannot tip yourself!")
                    || msg.startsWith("You can only use the /tip command")
                    || msg.startsWith("You can't tip the same person")
                    || msg.startsWith("You've already tipped someone in the past hour in ")
                    || msg.startsWith("You've already tipped that person")
                    || msg.startsWith("That player is not online, try another user!")) {
                event.setCancelled(true);
            }

            if (xpPattern.matcher(msg).matches()) {
                event.setCancelled(mOption.equals(MessageOption.COMPACT) || mOption.equals(MessageOption.HIDDEN));
                return;
            }

            Matcher playerMatcher = playerPattern.matcher(msg);
            if (playerMatcher.matches()) {
                TipTracker.addTip(playerMatcher.group("player"));
                event.setCancelled(mOption.equals(MessageOption.HIDDEN));
                return;
            }

            Matcher coinMatcher = coinPattern.matcher(msg);
            if (coinMatcher.matches()) {
                int coins = Integer.parseInt(coinMatcher.group("coins"));
                String game = coinMatcher.group("game");

                TipTracker.tipsSentEarnings.merge(game, coins, (a, b) -> a + b);
                event.setCancelled(mOption.equals(MessageOption.COMPACT) || mOption.equals(MessageOption.HIDDEN));

                return;
            }

            Matcher earnedMatcher = earnedPattern.matcher(msg);
            if (earnedMatcher.matches()) {
                int coins = Integer.parseInt(earnedMatcher.group("coins"));
                int xp = Integer.parseInt(earnedMatcher.group("xp"));
                String game = earnedMatcher.group("game");

                TipTracker.tipsReceivedEarnings.merge(game, coins, (a, b) -> a + b);
                TipTracker.tipsReceived += xp / 60;
                Writer.execute();

                if (mOption.equals(MessageOption.COMPACT)) {
                    ClientMessage.sendRaw(
                            String.format("%sEarned %s%d coins%s and %s%d experience%s in %s.",
                                    ChatColor.GREEN, ChatColor.YELLOW, coins,
                                    ChatColor.GREEN, ChatColor.BLUE, xp,
                                    ChatColor.GREEN, game
                            ));
                }
                event.setCancelled(mOption.equals(MessageOption.COMPACT) || mOption.equals(MessageOption.HIDDEN));
                System.out
                        .println("Earned " + coins + " coins and " + xp + " experience in " + game);
                return;
            }
        }

        if (LimboCommand.executed && msg.startsWith("A kick occurred in your connection") &&
                msg.contains("Illegal characters")) {
            event.setCancelled(true);
            LimboCommand.executed = false;
        }

    }
}
