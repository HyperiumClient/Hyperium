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

package cc.hyperium.mods.itemphysic;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

/**
 * @author KodingKing
 */
public class ItemPhysicMod extends AbstractMod {

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new EventHandlerLite());
        Hyperium.CONFIG.register(new ItemDummyContainer());

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Item Physic", "1.0", "CreativeMD");
    }
}
