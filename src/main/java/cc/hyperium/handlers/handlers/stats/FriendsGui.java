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

package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.HyperiumGui;
import club.sk1er.website.api.requests.HypixelApiPlayer;

public class FriendsGui extends HyperiumGui {
    private HypixelApiPlayer friends;

    public FriendsGui(HypixelApiPlayer guild) {
        this.friends = guild;
    }

    @Override
    protected void pack() {

    }
}
