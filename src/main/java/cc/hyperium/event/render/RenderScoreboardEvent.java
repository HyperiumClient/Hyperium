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
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when the scoreboard is rendered
 */
public final class RenderScoreboardEvent extends CancellableEvent {

    private final double x;
    private final double y;

    @NotNull
    private final ScoreObjective objective;

    @NotNull
    private final ScaledResolution resolution;

    public RenderScoreboardEvent(double x, double y, @NotNull ScoreObjective objective, @NotNull ScaledResolution resolution) {
        Preconditions.checkNotNull(objective, "objective");
        Preconditions.checkNotNull(resolution, "resolution");

        this.x = x;
        this.y = y;

        this.objective = objective;
        this.resolution = resolution;
    }

    @NotNull
    public final ScoreObjective getObjective() {
        return objective;
    }

    @NotNull
    public final ScaledResolution getResolution() {
        return resolution;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }
}
