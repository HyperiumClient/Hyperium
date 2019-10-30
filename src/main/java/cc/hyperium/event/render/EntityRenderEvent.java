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

package cc.hyperium.event.render;

import cc.hyperium.event.CancellableEvent;
import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class EntityRenderEvent extends CancellableEvent {

    @NotNull
    private final Entity entityIn;
    private final float posX;
    private final float posY;
    private final float posZ;
    private final float pitch;
    private final float yaw;
    private final float scale;

    public EntityRenderEvent(@NotNull Entity entityIn, float posX, float posY, float posZ, float pitch, float yaw, float scale) {
        Preconditions.checkNotNull(entityIn, "entityIn");

        this.entityIn = entityIn;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scale = scale;
    }

    @NotNull
    public final Entity getEntityIn() {
        return entityIn;
    }

    public final float getPosX() {
        return posX;
    }

    public final float getPosY() {
        return posY;
    }

    public final float getPosZ() {
        return posZ;
    }

    public final float getPitch() {
        return pitch;
    }

    public final float getYaw() {
        return yaw;
    }

    public final float getScale() {
        return scale;
    }
}
