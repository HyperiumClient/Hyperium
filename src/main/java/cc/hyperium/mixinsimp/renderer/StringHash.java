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

package cc.hyperium.mixinsimp.renderer;

import java.util.Objects;

public class StringHash {
    private int hash;

    public StringHash(String text, float red, float green, float blue, float alpha, boolean shadow) {
        this.hash = Objects.hash(text, red, green, blue, alpha, shadow);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StringHash)) return false;

        return Objects.equals(this.hash, ((StringHash) other).hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
