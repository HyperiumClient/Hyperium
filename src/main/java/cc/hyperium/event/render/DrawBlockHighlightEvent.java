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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.jetbrains.annotations.NotNull;

public final class DrawBlockHighlightEvent extends CancellableEvent {

    @NotNull
    private final EntityPlayer player;

    @NotNull
    private final MovingObjectPosition target;

    private final float partialTicks;

    public DrawBlockHighlightEvent(@NotNull EntityPlayer player, @NotNull MovingObjectPosition target, float partialTicks) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(target, "target");

        this.player = player;
        this.target = target;
        this.partialTicks = partialTicks;
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return player;
    }

    @NotNull
    public final MovingObjectPosition getTarget() {
        return target;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }
}
