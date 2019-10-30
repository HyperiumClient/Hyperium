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

package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;

import java.util.UUID;

import net.minecraft.entity.Entity;

import org.jetbrains.annotations.NotNull;

/**
 * Called when this player attacks an entity
 */
public final class PlayerAttackEntityEvent extends Event {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final Entity entity;

    public PlayerAttackEntityEvent(@NotNull UUID uuid, @NotNull Entity entity) {
        Preconditions.checkNotNull(uuid, "uuid");
        Preconditions.checkNotNull(entity, "entity");

        this.uuid = uuid;
        this.entity = entity;
    }

    @NotNull
    public final UUID getUuid() {
        return uuid;
    }

    @NotNull
    public final Entity getEntity() {
        return entity;
    }
}
