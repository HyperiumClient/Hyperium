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

import com.google.common.base.Preconditions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

import org.jetbrains.annotations.NotNull;

public final class LivingDeathEvent {

    @NotNull
    private final EntityLivingBase entity;

    @NotNull
    private final DamageSource cause;

    public LivingDeathEvent(@NotNull EntityLivingBase entity, @NotNull DamageSource cause) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(cause, "cause");

        this.entity = entity;
        this.cause = cause;
    }

    @NotNull
    public final EntityLivingBase getEntity() {
        return entity;
    }

    @NotNull
    public final DamageSource getCause() {
        return cause;
    }
}
