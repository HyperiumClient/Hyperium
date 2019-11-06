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
import net.minecraft.client.gui.ScaledResolution;

/**
 * Invoked when the hud of the client is rendered
 */
public class RenderHUDEvent extends Event {

    private final ScaledResolution resolution;
    private final float partialTicks;

    public RenderHUDEvent(ScaledResolution resolution, float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
