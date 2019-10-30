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

package cc.hyperium.handlers.handlers.data.leaderboards;

public class LeaderboardEntry {

    private int postion;
    private String value;
    private String playerUUID;
    private String playerDisplay;

    public LeaderboardEntry(int position, String value, String playerUUID, String playerDisplay) {
        postion = position;
        this.value = value;
        this.playerUUID = playerUUID;
        this.playerDisplay = playerDisplay;
    }

    public int getPostion() {

        return postion;
    }

    public String getValue() {
        return value;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerDisplay() {
        return playerDisplay;
    }

}
