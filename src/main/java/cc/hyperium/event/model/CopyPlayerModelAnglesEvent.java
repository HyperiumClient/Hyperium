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

package cc.hyperium.event.model;

import cc.hyperium.event.Event;
import cc.hyperium.mixinsimp.client.model.IMixinModelBiped;

import com.google.common.base.Preconditions;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class CopyPlayerModelAnglesEvent extends Event {

    @NotNull
    private final AbstractClientPlayer entity;
    @NotNull
    private final IMixinModelBiped model;

    public CopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(model, "model");

        this.entity = entity;
        this.model = model;
    }

    @NotNull
    public final AbstractClientPlayer getEntity() {
        return entity;
    }

    @NotNull
    public final IMixinModelBiped getModel() {
        return model;
    }
}
