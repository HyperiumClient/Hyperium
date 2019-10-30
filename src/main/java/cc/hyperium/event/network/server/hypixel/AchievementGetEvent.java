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

package cc.hyperium.event.network.server.hypixel;

import cc.hyperium.event.Event;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class AchievementGetEvent extends Event {

    @NotNull
    private final String achievement;

    public AchievementGetEvent(@NotNull String achievement) {
        Preconditions.checkNotNull(achievement, "achievement");
        this.achievement = achievement;
    }

    @NotNull
    public final String getAchievement() {
        return achievement;
    }
}
