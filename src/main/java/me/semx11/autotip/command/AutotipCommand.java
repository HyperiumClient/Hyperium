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

package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.event.HypixelListener;
import me.semx11.autotip.event.Tipper;
import me.semx11.autotip.misc.StartLogin;
import me.semx11.autotip.misc.Stats;
import me.semx11.autotip.misc.TipTracker;
import me.semx11.autotip.util.ChatColor;
import me.semx11.autotip.util.ClientMessage;
import me.semx11.autotip.util.FileUtil;
import me.semx11.autotip.util.Versions;
import net.minecraft.command.ICommandSender;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class AutotipCommand implements BaseCommand {

    public String getCommandName() {
        return "autotip";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "autotip <stats, info, messages, toggle, time>";
    }


    public void onExecute(String[] args) {

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "m":
                case "messages":
                    Autotip.messageOption = Autotip.messageOption.next();
                    ClientMessage.send("Tip Messages: " + Autotip.messageOption);
                    break;
                case "?":
                case "info":
                    ClientMessage.separator();
                    ClientMessage.send(
                            ChatColor.GOLD + "" + ChatColor.BOLD + "Autotip v" + Autotip.VERSION
                                    + " by Semx11",
                            null,
                            ChatColor.GOLD + "2Pi's legacy will live on."
                    );
                    ClientMessage.send("Running in " + "Hyperium Integration");
                    ClientMessage.send(
                            "Autotipper: " + (Autotip.toggle ? ChatColor.GREEN + "En"
                                    : ChatColor.RED + "Dis") + "abled");
                    ClientMessage.send("Tip Messages: " + Autotip.messageOption);
                    ClientMessage.send("Tips sent today: " + ChatColor.GOLD + TipTracker.tipsSent);
                    ClientMessage.send("Tips received today: " + ChatColor.GOLD
                            + TipTracker.tipsReceived);
                    ClientMessage
                            .send("Lifetime tips sent: " + ChatColor.GOLD + Autotip.totalTipsSent);
                    ClientMessage.send(ChatColor.GOLD
                            + "Type /autotip stats to see what has been earned.");
                    ClientMessage.separator();
                    break;
                case "s":
                case "stats":
                    LocalDate now = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    if (args.length == 2) {
                        switch (args[1].toLowerCase()) {
                            case "day":
                            case "daily":
                            case "today":
                                Stats.printStats(FileUtil.getDate());
                                break;
                            case "yesterday":
                                Stats.printStats(LocalDate.now().minusDays(1).format(formatter));
                                break;
                            case "week":
                            case "weekly":
                                Stats.printBetween(now.with(DayOfWeek.MONDAY).format(formatter),
                                        now.with(DayOfWeek.SUNDAY).format(formatter));
                                break;
                            case "month":
                            case "monthly":
                                Stats.printBetween(now.withDayOfMonth(1).format(formatter),
                                        now.withDayOfMonth(now.lengthOfMonth()).format(formatter));
                                break;
                            case "year":
                            case "yearly":
                                Stats.printBetween("01-01-" + Year.now().getValue(),
                                        "31-12-" + Year.now().getValue());
                                break;
                            case "all":
                            case "total":
                            case "life":
                            case "lifetime":
                                Stats.printBetween("25-06-2016", FileUtil.getDate());
                                break;
                            default:
                                ClientMessage.send(ChatColor.RED
                                        + "Usage: /autotip stats <day, week, month, year, lifetime>");
                                break;
                        }
                    } else {
                        Stats.printStats(FileUtil.getDate());
                    }
                    break;
                case "t":
                case "toggle":
                    Autotip.toggle = !Autotip.toggle;
                    ClientMessage.send(
                            "Autotipper: " + (Autotip.toggle ? ChatColor.GREEN + "En"
                                    : ChatColor.RED + "Dis") + "abled");
                    break;
                case "wave":
                case "time":
                    if (Autotip.toggle) {
                        if (Autotip.onHypixel) {
                            ClientMessage.separator();
                            ClientMessage.send("Last wave: " +
                                    ChatColor.GOLD + LocalTime.MIN.plusSeconds(Tipper.waveCounter)
                                    .toString());
                            ClientMessage.send("Next wave: " +
                                    ChatColor.GOLD + LocalTime.MIN.plusSeconds(
                                    Tipper.waveLength - Tipper.waveCounter).toString());
                            ClientMessage.separator();
                        } else {
                            ClientMessage
                                    .send("Autotip is disabled as you are not playing on Hypixel.");
                        }
                    } else {
                        ClientMessage.send("Autotip is disabled. Use " + ChatColor.GOLD
                                + "/autotip toggle"
                                + ChatColor.GRAY + " to enable it.");
                    }
                    break;
                case "whatsnew":
                    ClientMessage.separator();
                    ClientMessage.send(ChatColor.GOLD + "What's new in Autotip v" + Autotip.VERSION
                            + ":");
                    Versions.getInstance().getInfoByVersion(Autotip.VERSION).getChangelog().forEach(
                            s -> ClientMessage
                                    .send(ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + s));
                    ClientMessage.separator();
                    break;
                case "update":
                    StartLogin.login();
                    break;
                case "info+":
                    ClientMessage.separator();
                    ClientMessage.send("Last IP joined: " + HypixelListener.lastIp);
                    ClientMessage.send("Detected MC version: Hyperium");
                    ClientMessage
                            .send("Current tipqueue: " + StringUtils.join(Tipper.tipQueue, ", "));
                    ClientMessage.separator();
                    break;
                default:
                    ClientMessage.send(ChatColor.RED + "Usage: " + getUsage());
                    break;
            }
        } else {
            ClientMessage.send(ChatColor.RED + "Usage: " + getUsage());
        }
    }


    @Override
    public String getName() {
        return getCommandName();
    }

    @Override
    public String getUsage() {
        return getCommandUsage(null);
    }

}
