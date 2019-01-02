/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.cosmetics;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
class KillCounterMuscles extends AbstractCosmetic {
    public KillCounterMuscles() {
        super(false, EnumPurchaseType.KILL_TRACKER_MUSCLE);
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {

    }

}
