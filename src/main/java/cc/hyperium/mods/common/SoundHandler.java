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

package cc.hyperium.mods.common;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.world.audio.SoundPlayEvent;
import cc.hyperium.mods.AbstractMod;

public class SoundHandler extends AbstractMod {

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void playSound(SoundPlayEvent event) {
        if (event.getSound().getSoundLocation().getResourcePath().equals("portal.portal") && Settings.PORTAL_SOUNDS) {
            event.setCancelled(true);
        }
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Portal Muter", "1.0", "asbyth");
    }
}
