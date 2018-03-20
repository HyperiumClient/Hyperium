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

import java.util.regex.Pattern;

public enum MinecraftVersion {
    V1_8, V1_8_8, V1_8_9, V1_9, V1_9_4, V1_10, V1_10_2, V1_11, V1_11_2;

    public static MinecraftVersion fromString(String version) throws IllegalArgumentException {
        Pattern p = Pattern
                .compile("^(1\\.8(\\.[8-9])?|1\\.9(\\.4)?|1\\.10(\\.2)?|1\\.11(\\.2)?)$");
        if (p.matcher(version).matches()) {
            return valueOf("V" + version.replaceAll("\\.", "_"));
        }
        return null;
    }

    public String toString() {
        return this.name().substring(1).replaceAll("_", ".");
    }

}
