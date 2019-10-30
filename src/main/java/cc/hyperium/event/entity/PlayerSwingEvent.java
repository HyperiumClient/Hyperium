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

import java.util.UUID;

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked once player swings
 */
public final class PlayerSwingEvent extends Event {

    @NotNull
    private final UUID player;

    @NotNull
    private final Vec3 posVec;

    @NotNull
    private final Vec3 lookVec;

    @NotNull
    private final BlockPos pos;

    public PlayerSwingEvent(@NotNull UUID player, @NotNull Vec3 posVec, @NotNull Vec3 lookVec, @NotNull BlockPos pos) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(posVec, "posVec");
        Preconditions.checkNotNull(lookVec, "lookVec");
        Preconditions.checkNotNull(pos, "pos");

        this.player = player;
        this.posVec = posVec;
        this.lookVec = lookVec;
        this.pos = pos;
    }

    @NotNull
    public final UUID getPlayer() {
        return player;
    }

    @NotNull
    public final Vec3 getPosVec() {
        return posVec;
    }

    @NotNull
    public final Vec3 getLookVec() {
        return lookVec;
    }

    @NotNull
    public final BlockPos getPos() {
        return pos;
    }
}
