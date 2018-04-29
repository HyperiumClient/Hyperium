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

package me.semx11.autotip.misc;

import cc.hyperium.utils.ChatColor;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.ClientMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Stats {

    private static LocalDate upgradeDate;

    public static void setUpgradeDate(LocalDate date) {
        upgradeDate = date;
    }

    public static void printBetween(String s, String e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate start = LocalDate.parse(s, formatter);
        LocalDate end = LocalDate.parse(e, formatter);

        List<String> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start.format(formatter));
            start = start.plusDays(1);
        }
        printStats(totalDates.toArray(new String[0]));
    }

    public static void printStats(String... days) {

        Map<String, Integer> totalStats = new HashMap<>();
        Map<String, Integer> sentStats = new HashMap<>();
        Map<String, Integer> receivedStats = new HashMap<>();

        int[] xp = {0, 0};
        int[] tips = {0, 0};

        for (String date : days) {
            File f = new File(Autotip.USER_DIR + "stats" + File.separator + date + ".at");
            if (!f.exists()) {
                continue;
            }

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            final boolean oldTips = localDate.isBefore(LocalDate.of(2016, 11, 29));
            final boolean fixTips = localDate.isAfter(LocalDate.of(2016, 11, 29)) && localDate
                    .isBefore(upgradeDate);

            List<Map<String, Integer>> dailyStats = getDailyStats(f);

            dailyStats.get(0).forEach((game, coins) -> {
                if (game.equals("tips")) {
                    xp[0] += 50 * coins;
                    tips[0] += coins;
                } else {
                    totalStats.merge(game, coins, (a, b) -> a + b);
                    sentStats.merge(game, coins, (a, b) -> a + b);
                }
            });
            dailyStats.get(1).forEach((game, coins) -> {
                if (game.equals("tips")) {
                    if (fixTips) {
                        coins = coins / 2;
                    }
                    xp[1] += (oldTips ? 30 : 60) * coins;
                    tips[1] += coins;
                } else {
                    totalStats.merge(game, coins, (a, b) -> a + b);
                    receivedStats.merge(game, coins, (a, b) -> a + b);
                }
            });
        }

        int karma = 0;
        if (sentStats.containsKey("karma")) {
            karma = sentStats.get("karma");
            sentStats.remove("karma");
        }

        List<String> games = totalStats.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!games.isEmpty()) {
            ClientMessage.separator();
            games.forEach(game -> {
                int sentCoins = sentStats.getOrDefault(game, 0);
                int receivedCoins = receivedStats.getOrDefault(game, 0);
                if (sentStats.containsKey(game) || receivedStats.containsKey(game)) {
                    ClientMessage.send(
                            String.format("%s%s: %s%s coins",
                                    ChatColor.GREEN, game,
                                    ChatColor.YELLOW, format(sentCoins + receivedCoins)),
                            null,
                            String.format(
                                    "%s%s\n%sBy sending: %s%s coins\n%sBy receiving: %s%s coins",
                                    ChatColor.GREEN, game,
                                    ChatColor.RED,
                                    ChatColor.YELLOW, format(sentCoins),
                                    ChatColor.BLUE,
                                    ChatColor.YELLOW, format(receivedCoins))
                    );
                }
            });
            ClientMessage.send(
                    String.format("%sTips: %s", ChatColor.GOLD, format(tips[0] + tips[1])),
                    null,
                    String.format("%sSent: %s%s tips\n%sReceived: %s%s tips",
                            ChatColor.RED,
                            ChatColor.GOLD, format(tips[0]),
                            ChatColor.BLUE,
                            ChatColor.GOLD, format(tips[1]))
            );
            ClientMessage.send(
                    String.format("%sXP: %s", ChatColor.BLUE, format(xp[0] + xp[1])),
                    null,
                    String.format("%sBy sending: %s%s XP\n%sBy receiving: %s XP",
                            ChatColor.RED,
                            ChatColor.BLUE, format(xp[0]),
                            ChatColor.BLUE, format(xp[1]))
            );
            if (karma > 0) {
                ClientMessage.send(
                        String.format("%sKarma: %s", ChatColor.LIGHT_PURPLE, format(karma)),
                        null,
                        ChatColor.LIGHT_PURPLE + "Welcome to the veteran club :)"
                );
            }

            ClientMessage.send(String.format("Stats from %s%s",
                    days[0].replace("-", "/"),
                    days.length > 1 ? " - " + days[days.length - 1].replace("-", "/") : ""
            ));
            ClientMessage.separator();
        } else {
            ClientMessage.send(ChatColor.RED + "You have never tipped someone in this period!");
            ClientMessage.send(String.format("(%s%s)",
                    days[0].replace("-", "/"),
                    days.length > 1 ? " - " + days[days.length - 1].replace("-", "/") : ""
            ));
        }
    }

    private static List<Map<String, Integer>> getDailyStats(File file) {
        try {
            Map<String, Integer> sentStats = new HashMap<>();
            Map<String, Integer> receivedStats = new HashMap<>();
            try (BufferedReader statsReader = new BufferedReader(new FileReader(file.getPath()))) {
                List<String> lines = statsReader.lines().collect(Collectors.toList());
                if (lines.size() >= 2) {
                    String[] tipStats = lines.get(0).split(":");

                    sentStats.put("tips", Integer.parseInt(tipStats[0]));
                    sentStats.put("karma", Integer.parseInt(lines.get(1)));

                    receivedStats
                            .put("tips", tipStats.length > 1 ? Integer.parseInt(tipStats[1]) : 0);

                    lines.stream().skip(2).forEach(line -> {
                        String stats[] = line.split(":");
                        if (!stats[1].equals("0")) {
                            sentStats.put(stats[0], Integer.parseInt(stats[1]));
                        }
                        if (stats.length > 2 && !stats[2].equals("0")) {
                            receivedStats.put(stats[0], Integer.parseInt(stats[2]));
                        }
                    });
                }
            }
            return Arrays.asList(sentStats, receivedStats);
        } catch (IOException e) {
            e.printStackTrace();
            return Arrays.asList(Collections.emptyMap(), Collections.emptyMap());
        }
    }

    private static String format(int number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        return formatter.format(number);
    }

}