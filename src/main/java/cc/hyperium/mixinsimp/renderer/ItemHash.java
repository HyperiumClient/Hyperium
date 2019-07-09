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

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class ItemHash {
    private int hash;

    public ItemHash(IBakedModel model, int color, String unlocalized, int damage, int meta, NBTTagCompound compound) {
        this.hash = Objects.hash(model, color, unlocalized, damage, meta, compound);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemHash)) return false;

        return Objects.equals(this.hash, ((ItemHash) other).hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
