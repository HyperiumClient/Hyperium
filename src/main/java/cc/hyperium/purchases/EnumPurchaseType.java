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
    PARTICLE_BACKGROUND("Particle Background"),
    WING_COSMETIC("wings"),
    KILL_TRACKER_MUSCLE("Muscle Kill Tracker"),
    DAB_ON_KILL("Dab on Kill"),
    CHROMA_WIN("Chroma on Win"),
    LEVEL_HEAD("Custom Levelhead"),
    DEAL_WITH_IT("Deal With It Glasses"),
    FLIP_COSMETIC("Flip Cosmetic"),
    DEADMAU5_COSMETIC("Ears"),
    DRAGON_HEAD("Dragon Head"),
    UNKNOWN("Unknown");

    private String displayName;

    EnumPurchaseType(String displayName) {
        this.displayName = displayName;
    }

    public static EnumPurchaseType parse(String asString) {
        try {
            return valueOf(asString.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

}
