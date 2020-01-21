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

package cc.hyperium.utils.statistics;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.network.server.hypixel.HypixelGetCoinsEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.hypixel.HypixelGetXPEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralStatisticsTracking {

    public static final DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    // These can be put in a HUD or printed in a command somewhere.
    @ConfigOpt public static int lifetimeCoins;
    @ConfigOpt public static int monthlyCoins;
    @ConfigOpt public static int dailyCoins;
    @ConfigOpt public static int dailyXP;
    @ConfigOpt public String currentDateString = format.format(new Date());


    @InvokeEvent
    public void onChat(ChatEvent event) {
        // Check how much the time has changed since the last use.
        checkTimes();

        String message = event.getChat().getUnformattedText();
        for (String line : message.split("\n")) {
            if(line.startsWith("§r     •")) {
                if (line.contains("Coins")) {
                    int coins = Integer.parseInt(line.split("• §6")[1].split(" Coins")[0].replaceAll("[\\D]", ""));
                    EventBus.INSTANCE.post(new HypixelGetCoinsEvent(coins));
                    // Increment coin counters.
                    lifetimeCoins += coins;
                    monthlyCoins += coins;
                    dailyCoins += coins;
                } else if (line.contains("Hypixel Experience")){
                    int exp = Integer.parseInt(line.split("• §3")[1].split(" Hypixel Experience")[0].replaceAll("[\\D]", ""));
                    EventBus.INSTANCE.post(new HypixelGetXPEvent(exp));
                    dailyXP += exp;
                }
            }
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
            dailyXP = 0;
            currentDateString = format.format(currentDate);
        }

        // If the month has changed, reset monthly coins and daily coins.
        if (!currentMonths.equals(months)) {
            dailyCoins = 0;
            dailyXP = 0;
            monthlyCoins = 0;
            currentDateString = format.format(currentDate);
        }
    }
}
