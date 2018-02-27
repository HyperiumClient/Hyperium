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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import cc.hyperium.event.SingleplayerJoinEvent;

import java.util.regex.Pattern;

public class HypixelDetector {

    private static final Pattern HYPIXEL_PATTERN =
            Pattern.compile("^(?:(?:(?:\\w+\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3}))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);
    private static HypixelDetector instance;
    private boolean hypixel = false;

    public HypixelDetector() {
        instance = this;
    }

    public static HypixelDetector getInstance() {
        return instance;
    }

    @InvokeEvent
    public void serverJoinEvent(ServerJoinEvent event) {
        boolean h1 = this.hypixel;
        this.hypixel = HYPIXEL_PATTERN.matcher(event.getServer()).find();
        Hyperium.INSTANCE.getNotification().display("Hypixel", "Welcome to the HYPIXEL ZOO", 5f);
    }

    @InvokeEvent
    public void serverLeaveEvent(ServerLeaveEvent event) {
        hypixel = false;
    }

    @InvokeEvent
    public void singlePlayerJoin(SingleplayerJoinEvent event) {
        hypixel = false;
    }

    public boolean isHypixel() {
        return hypixel;
    }
}
