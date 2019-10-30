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

package club.sk1er.website.api.requests;

import cc.hyperium.utils.JsonHolder;

public class HypixelApiFriendObject {
    private final JsonHolder data;
    private int ord;

    public HypixelApiFriendObject(JsonHolder data) {
        this.data = data;

        switch (data.optString("rank")) {
            case "ADMIN":
                ord = 1;
                break;
            case "MODERATOR":
                ord = 2;
                break;
            case "HELPER":
                ord = 3;
                break;
            case "YOUTUBER":
                ord = 4;
                break;
            case "MVP_PLUS_PLUS":
                ord = 5;
                break;
            case "MVP_PLUS":
                ord = 6;
                break;
            case "MVP":
                ord = 7;
                break;
            case "VIP_PLUS":
                ord = 8;
                break;
            case "VIP":
                ord = 9;
                break;
            default:
                ord = 11;
        }
    }

    public String getDisplay() {
        return data.optString("display");
    }

    public String getName() {
        return data.optString("name");
    }

    public int rankOrdinal() {
        return ord;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HypixelApiFriendObject && ((HypixelApiFriendObject) obj).getUuid().equals(getUuid());
    }

    public long getLogoff() {
        return data.optLong("logoff");
    }

    public String getUuid() {
        return data.optString("uuid");
    }

    public long getAddedOn() {
        return data.optLong("time");
    }

    public String getRank() {
        return data.optString("rank");
    }
}
