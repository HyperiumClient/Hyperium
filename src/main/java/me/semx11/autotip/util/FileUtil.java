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

package me.semx11.autotip.util;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.misc.Stats;
import me.semx11.autotip.misc.TipTracker;
import me.semx11.autotip.misc.Writer;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class FileUtil {

    /*
    mods/autotip/
    -- stats (dir)
    -- options.at
    -- tipped.at

    mods/autotip/uuid/
    -- stats (dir)
    -- options.at
    -- tipped.at
    */
    /*
    if (exists(user_dir) && (exists(oldDir + "stats") || exists(oldDir + "options.at") || exists(oldDir + "tipped.at"))) {
                FileUtils.copyDirectory(Paths.get(user_dir).toFile(), Paths.get("mods/autotip_temp").toFile());
                FileUtils.deleteDirectory(Paths.get(user_dir).toFile());
            }

            if (!exists(user_dir)) FileUtils.copyDirectory(Paths.get("mods/autotip").toFile(), Paths.get("mods/autotip/uuid").toFile());

            if (exists(user_dir + "stats")) FileUtils.deleteDirectory(Paths.get(oldDir + "stats").toFile());
            if (exists(user_dir + "options.at")) Files.deleteIfExists(Paths.get(oldDir + "options.at"));
            if (exists(user_dir + "tipped.at")) Files.deleteIfExists(Paths.get(oldDir + "tipped.at"));
     */

    public static void getVars() throws IOException {
        try {
            File statsDir = new File(Autotip.USER_DIR + "stats");

            if (!statsDir.exists()) {
                statsDir.mkdirs();
            }

            if (exists(Autotip.USER_DIR + "upgrade-date.at")) {
                String explainCode = "I had to write this crappy code because 2pi didn't learn about proper serialization.";
                String date = FileUtils
                        .readFileToString(Paths.get(Autotip.USER_DIR + "upgrade-date.at").toFile());
                LocalDate parsed;
                try {
                    parsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                    parsed = LocalDate.now();
                }
                Stats.setUpgradeDate(parsed);
            } else {
                LocalDate date = LocalDate.now().plusDays(1);
                String dateString = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
                FileUtils
                        .writeStringToFile(Paths.get(Autotip.USER_DIR + "upgrade-date.at").toFile(),
                                dateString);
                Stats.setUpgradeDate(date);
            }

            // String oldDir = "mods" + File.separator + "autotip" + File.separator;
            boolean executeWriter = false;

//            if (exists(Autotip.USER_DIR) && (exists(oldDir + "stats") || exists(oldDir + "options.at") || exists(
//                    oldDir + "tipped.at"))) {
//                FileUtils.copyDirectory(Paths.get(Autotip.USER_DIR).toFile(),
//                                        Paths.get("mods" + File.separator + "autotip_temp").toFile());
//                FileUtils.deleteDirectory(Paths.get(Autotip.USER_DIR).toFile());
//            }

//            if (!exists(Autotip.USER_DIR))
//                FileUtils.copyDirectory(Paths.get(oldDir).toFile(), Paths.get(Autotip.USER_DIR).toFile());
//
//            if (exists(Autotip.USER_DIR + "stats") && exists(oldDir + "stats"))
//                FileUtils.deleteDirectory(Paths.get(oldDir + "stats").toFile());
//            if (exists(Autotip.USER_DIR + "options.at") && exists(oldDir + "options.at"))
//                Files.deleteIfExists(Paths.get(oldDir + "options.at"));
//            if (exists(Autotip.USER_DIR + "tipped.at") && exists(oldDir + "tipped.at"))
//                Files.deleteIfExists(Paths.get(oldDir + "tipped.at"));

            if (exists(Autotip.USER_DIR + "options.at")) {
                try (BufferedReader readOptions = new BufferedReader(
                        new FileReader(Autotip.USER_DIR + "options.at"))) {
                    List<String> lines = readOptions.lines().collect(Collectors.toList());
                    if (lines.size() >= 4) {
                        Autotip.toggle = Boolean.parseBoolean(lines.get(0)); // mod enabled
                        String chatSetting = lines.get(1);
                        switch (chatSetting) {
                            case "true":
                            case "false":
                                Autotip.messageOption = Boolean.parseBoolean(chatSetting)
                                        ? MessageOption.SHOWN
                                        : MessageOption.COMPACT;
                                break;
                            case "SHOWN":
                            case "COMPACT":
                            case "HIDDEN":
                                Autotip.messageOption = MessageOption.valueOf(chatSetting);
                                break;
                            default:
                                Autotip.messageOption = MessageOption.SHOWN;
                        }
                        //lines.get(2); // anonymous tips // does exist, but no use anymore
                        try {
                            Autotip.totalTipsSent = Integer.parseInt(lines.get(3));
                        } catch (NumberFormatException e) {
                            Autotip.totalTipsSent = 0;
                            executeWriter = true;
                        }
                    } else {
                        executeWriter = true;
                    }
                }
            } else {
                executeWriter = true;
            }

            if (exists(Autotip.USER_DIR + "stats" + File.separator + getDate() + ".at")) {
                try (BufferedReader readDaily = new BufferedReader(
                        new FileReader(
                                Autotip.USER_DIR + "stats" + File.separator + getDate() + ".at"))) {
                    List<String> lines = readDaily.lines().collect(Collectors.toList());
                    if (lines.size() >= 2) {
                        String[] tipStats = lines.get(0).split(":");
                        TipTracker.tipsSent = Integer.parseInt(tipStats[0]);
                        TipTracker.tipsReceived =
                                tipStats.length > 1 ? Integer.parseInt(tipStats[1]) : 0;
                        TipTracker.karmaCount = Integer.parseInt(lines.get(1));
                        lines.stream().skip(2).forEach(line -> {
                            String[] stats = line.split(":");
                            TipTracker.tipsSentEarnings.put(stats[0], Integer.parseInt(stats[1]));
                            if (stats.length > 2) {
                                TipTracker.tipsReceivedEarnings
                                        .put(stats[0], Integer.parseInt(stats[2]));
                            }
                        });
                    }
                }
            } else {
                executeWriter = true;
            }

            if (new File(Autotip.USER_DIR + "tipped.at").exists()) {
                try (BufferedReader f = new BufferedReader(
                        new FileReader(Autotip.USER_DIR + "tipped.at"))) {
                    List<String> lines = f.lines().collect(Collectors.toList());
                    if (lines.size() >= 1) {
                        String date = lines.get(0);
                        if (Objects.equals(date, getDate())) {
                            lines.stream().skip(1).forEach(line -> Autotip.alreadyTipped.add(line));
                        } else {
                            executeWriter = true;
                        }
                    } else {
                        executeWriter = true;
                    }
                }
            } else {
                executeWriter = true;
            }

            if (executeWriter) {
                Writer.execute();
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    public static String getDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static String getServerDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        return sdf.format(new Date());
    }

}