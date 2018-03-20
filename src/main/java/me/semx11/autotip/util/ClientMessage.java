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

package me.semx11.autotip.util;

import cc.hyperium.utils.ChatColor;

public class ClientMessage {

    private static String prefix =
            ChatColor.GOLD + "A" + ChatColor.YELLOW + "T" + ChatColor.DARK_GRAY + " > "
                    + ChatColor.GRAY;

    public static void send(String msg) {
        UniversalUtil.chatMessage(prefix + msg);
    }

    public static void send(String msg, String url, String hoverText) {
        UniversalUtil.chatMessage(prefix + msg, url, hoverText);
    }

    public static void sendRaw(String msg) {
        UniversalUtil.chatMessage(msg);
    }

    public static void separator() {
        UniversalUtil.chatMessage(
                ChatColor.GOLD + "" + ChatColor.BOLD + "----------------------------------");
    }

}
