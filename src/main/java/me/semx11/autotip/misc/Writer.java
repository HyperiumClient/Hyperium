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

import me.semx11.autotip.Autotip;
import me.semx11.autotip.util.FileUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Writer implements Runnable {

    private static String lastDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private static final String ls = System.lineSeparator();

    public static void execute() {
        Autotip.THREAD_POOL.submit(new Writer());
    }

    @Override
    public void run() {
        try {
            try (FileWriter writeOptions = new FileWriter(Autotip.USER_DIR + "options.at")) {
                write(writeOptions, Autotip.toggle + ls);
                write(writeOptions, Autotip.messageOption.name() + ls);
                write(writeOptions, "true" + ls);
                write(writeOptions, Autotip.totalTipsSent + ls);
            }

            if (!lastDate.equals(FileUtil.getDate())) {
                TipTracker.tipsSent = 0;
                TipTracker.tipsReceived = 0;
                TipTracker.tipsSentEarnings.clear();
                TipTracker.tipsReceivedEarnings.clear();
            }

            FileWriter dailyStats = new FileWriter(
                    Autotip.USER_DIR + "stats" + File.separator + FileUtil.getDate() + ".at");
            write(dailyStats, TipTracker.tipsSent + ":" + TipTracker.tipsReceived + ls);
            write(dailyStats, "0" + ls);

            List<String> games = Stream.concat(
                    TipTracker.tipsSentEarnings.keySet().stream(),
                    TipTracker.tipsReceivedEarnings.keySet().stream()
            ).distinct().collect(Collectors.toList());

            games.forEach(game -> {
                int sent =
                        TipTracker.tipsSentEarnings
                                .getOrDefault(game, 0);
                int received = TipTracker.tipsReceivedEarnings.getOrDefault(
                game, 0);
                write(dailyStats, game + ":" + sent + ":" + received + ls);
            });
            dailyStats.close();

            lastDate = FileUtil.getDate();

            if (new File(Autotip.USER_DIR + "tipped.at").exists()) {
                try (BufferedReader f = new BufferedReader(
                        new FileReader(Autotip.USER_DIR + "tipped.at"))) {
                    List<String> lines = f.lines().collect(Collectors.toList());
                    if (lines.size() >= 1) {
                        String date = lines.get(0);
                        if (!Objects.equals(date, FileUtil.getServerDate())) {
                            Autotip.alreadyTipped.clear();
                        }
                    }
                }
            }
            try (FileWriter tippedNames = new FileWriter(Autotip.USER_DIR + "tipped.at")) {
                write(tippedNames, FileUtil.getServerDate() + ls);
                for (String name : Autotip.alreadyTipped) {
                    write(tippedNames, name + ls);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(FileWriter writer, String text) {
        try {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}