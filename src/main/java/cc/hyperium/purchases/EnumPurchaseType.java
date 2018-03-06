/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.purchases;

public enum EnumPurchaseType {
    LEVEL_HEAD(2709838),
    EARLY_BIRD(2888258),
    //set to 0 because 0 is default value of a primitive !
    UNKNOWN(0);

    private int id;

    EnumPurchaseType(int i) {
        this.id = i;
    }

    public static EnumPurchaseType parse(String asString) {
        try {
            return valueOf(asString.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public int getId() {
        return id;
    }
}
