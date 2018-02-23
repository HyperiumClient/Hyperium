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

public class TimeUtil {

    private final static long ONE_SECOND = 1000;
    private final static long ONE_MINUTE = ONE_SECOND * 60;
    private final static long ONE_HOUR = ONE_MINUTE * 60;
    private final static long ONE_DAY = ONE_HOUR * 24;

    public static String formatMillis(long duration) {
        StringBuilder sb = new StringBuilder();
        long temp;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                sb.append(temp).append(" day").append(temp > 1 ? "s" : "");
                return sb.toString() + " ago";
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                sb.append(temp).append(" hour").append(temp > 1 ? "s" : "");
                return sb.toString() + " ago";
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                sb.append(temp).append(" minute").append(temp > 1 ? "s" : "");
                return sb.toString() + " ago";
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                sb.append(temp).append(" second").append(temp > 1 ? "s" : "");
            }
            return sb.toString() + " ago";
        } else {
            return "just now";
        }
    }
}
