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

package cc.hyperium.mods.oldanimations;

import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;

public class OldAnimations extends AbstractMod {

    private final Metadata metadata;

    public OldAnimations() {
        Metadata data = new Metadata(this, "OldAnimations", "1.0", "Amplifiable");
        data.setDisplayName(ChatColor.AQUA + "OldAnimations");
        metadata = data;
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new AnimationEventHandler());
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return metadata;
    }
}
