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

import cc.hyperium.event.model.CopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.client.model.IMixinModelBiped;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Get called before the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need the same roations
 */
public final class PreCopyPlayerModelAnglesEvent extends CopyPlayerModelAnglesEvent {

    public PreCopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        super(entity, model);

    }
}
