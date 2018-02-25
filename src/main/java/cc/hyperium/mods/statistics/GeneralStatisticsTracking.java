/*
 * Hyperium Client, Free client with huds and popular mod
 * Copyright (C) 2018  Hyperium Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.hyperium.mods.statistics;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralStatisticsTracking {

    public static DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    // These can be put in a HUD or printed in a command somewhere.

    @ConfigOpt
    public static int lifetimeCoins = 0;
    @ConfigOpt
    public static int monthlyCoins = 0;
    @ConfigOpt
    public static int dailyCoins = 0;

    @ConfigOpt
    public String currentDateString = format.format(new Date());


    @InvokeEvent
    public void onChat(ChatEvent event) {
        // Check how much the time has changed since the last use.
        checkTimes();


        String line = event.getChat().getUnformattedText();
        if (line.startsWith("+") && line.contains("coins")) {
            int coins = Integer.parseInt(line.split("\\+")[1].split(" coins")[0]);

            // Increment coin counters.
            lifetimeCoins += coins;
            monthlyCoins += coins;
            dailyCoins += coins;
            System.out.println("Lifetime coins earned: " + lifetimeCoins);
        }
    }

    public void checkTimes() {
        String days = currentDateString.split("/")[2];
        String months = currentDateString.split("/")[1];

        Date currentDate = new Date();
        String dateString = format.format(currentDate);
        String currentDays = dateString.split("/")[2];
        String currentMonths = dateString.split("/")[1];

        // If the day has changed, reset daily coins.
        if (!currentDays.equals(days)) {
            dailyCoins = 0;
            currentDateString = format.format(currentDate);
        }

        // If the month has changed, reset monthly coins and daily coins.
        if (!currentMonths.equals(months)) {
            dailyCoins = 0;
            monthlyCoins = 0;
            currentDateString = format.format(currentDate);
        }
    }
}
