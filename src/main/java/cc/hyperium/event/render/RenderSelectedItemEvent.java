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

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when the selected item is about to be rendered
 */
public final class RenderSelectedItemEvent extends Event {

    @NotNull
    private final ScaledResolution scaledRes;

    public RenderSelectedItemEvent(@NotNull ScaledResolution scaledRes) {
        Preconditions.checkNotNull(scaledRes, "scaledRes");

        this.scaledRes = scaledRes;
    }

    @NotNull
    public final ScaledResolution getScaledRes() {
        return scaledRes;
    }
}
