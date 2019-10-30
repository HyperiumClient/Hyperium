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

import java.util.List;

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemTooltipEvent extends Event {

    @NotNull
    private final ItemStack item;
    @NotNull
    private final List toolTip;

    public ItemTooltipEvent(@NotNull ItemStack item, @NotNull List toolTip) {
        Preconditions.checkNotNull(item, "item");
        Preconditions.checkNotNull(toolTip, "toolTip");

        this.item = item;
        this.toolTip = toolTip;
    }

    @NotNull
    public final ItemStack getItem() {
        return item;
    }

    @NotNull
    public final List getToolTip() {
        return toolTip;
    }
}
