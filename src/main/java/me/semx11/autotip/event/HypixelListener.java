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

package me.semx11.autotip.event;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.misc.StartLogin;
import me.semx11.autotip.util.UniversalUtil;

public class HypixelListener {

    public static String lastIp;

    @InvokeEvent
    public void playerLoggedIn(ServerJoinEvent event) {
        lastIp = UniversalUtil.getRemoteAddress(event).toLowerCase();
        if (lastIp.contains(".hypixel.net") || lastIp.contains("209.222.115.14")) {
            Autotip.onHypixel = true;
            Tipper.waveCounter = 910;
            Autotip.THREAD_POOL.submit(new StartLogin());
        } else {
            Autotip.onHypixel = false;
        }
    }

    @InvokeEvent
    public void playerLoggedOut(ServerLeaveEvent event) {
        Autotip.onHypixel = false;
    }

}