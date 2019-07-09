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

package cc.hyperium.mixins.entity;

import cc.hyperium.mixinsimp.entity.HyperiumEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public double posX;
    @Shadow public double posY;
    @Shadow public double posZ;
    @Shadow public abstract String getName();
    @Shadow public abstract UUID getUniqueID();
    @Shadow public Vec3 getLook(float partialTicks) {
        return null;
    }
    @Shadow public abstract boolean isSneaking();

    private HyperiumEntity hyperiumEntity = new HyperiumEntity((Entity) (Object) this);

    /**
     * @author Sk1er
     * @reason Cache render strings
     */
    @Overwrite
    public IChatComponent getDisplayName() {
        return hyperiumEntity.getDisplayName();
    }
}
