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

package cc.hyperium.purchases;

import java.util.Arrays;

public enum EnumPurchaseType {
    PARTICLE_BACKGROUND("Particle Background"),
    WING_COSMETIC("wings"),
    CHROMA_WIN("Chroma on Win"),
    LEVEL_HEAD("Custom Levelhead"),
    FLIP_COSMETIC("Flip Cosmetic"),
    BUTT("Butt"),
    DEADMAU5_COSMETIC("Ears"),
    DRAGON_HEAD("Dragon Head"),
    DRAGON_COMPANION("Dragon Companion"),
    BACKPACK_ENDER_DRAGON("Ender Dragon Backpack"),
    HAT_TOPHAT("Tophat"),
    HAT_FEZ("Fez Hat"),
    HAT_LEGO("Lego Hat"),
    HAMSTER_COMPANION("Hamster Companion"),
    UNKNOWN("Unknown");

    private String displayName;

    EnumPurchaseType(String displayName) {
        this.displayName = displayName;
    }

    public static EnumPurchaseType parse(String asString) {
        try {
            return valueOf(asString.toUpperCase());
        } catch (Exception e) {
            return Arrays.stream(values()).filter(enumPurchaseType -> enumPurchaseType.displayName.equalsIgnoreCase(asString)).findFirst().orElse(UNKNOWN);
        }
    }

    public String getDisplayName() {
        return displayName;
    }

}
