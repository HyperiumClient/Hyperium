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

package cc.hyperium.event.world.item;

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

public final class ItemPickupEvent extends Event {

    @NotNull
    private final EntityPlayer player;

    @NotNull
    private final EntityItem item;

    public ItemPickupEvent(@NotNull EntityPlayer player, @NotNull EntityItem item) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(item, "item");

        this.player = player;
        this.item = item;
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return player;
    }

    @NotNull
    public final EntityItem getItem() {
        return item;
    }
}
