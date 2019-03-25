/*
 *     Copyright (C) 2019  Hyperium <https://hyperium.cc/>
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

package cc.hyperium.mods.dcprevent;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;

public class DCPrevent extends AbstractMod {

    private boolean hasClickedThisTick;

    @Override
    public AbstractMod init() {
        hasClickedThisTick = false;
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Double Click Prevent", "1.0", "asbyth");
    }

    @InvokeEvent
    public void mouseClick(MouseButtonEvent event) {
        if (event.getState() && hasClickedThisTick && Settings.PREVENT_DOUBLECLICK) {
            event.setCancelled(true);
            return;
        }

        if (event.getState()) {
            hasClickedThisTick = true;
        }
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        hasClickedThisTick = false;
    }
}
