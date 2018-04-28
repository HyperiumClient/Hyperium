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

package cc.hyperium.purchases;

public enum EnumPurchaseType {
    PARTICLE_BACKGROUND,
    WING_COSMETIC,
    KILL_TRACKER_MUSCLE,
    DAB_ON_KILL,
    CHROMA_WIN,
    LEVEL_HEAD,
    DEAL_WITH_IT,
    FLIP_COSMETIC,
    DEADMAU5_COSMETIC,
    UNKNOWN;


    EnumPurchaseType() {

    }

    public static EnumPurchaseType parse(String asString) {
        try {
            return valueOf(asString.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

}
