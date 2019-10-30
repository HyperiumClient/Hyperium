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

package cc.hyperium.event.interact;

import cc.hyperium.event.Event;

/**
 * Invoked once a key is pressed
 */
public class KeyPressEvent extends Event {

    private final int key;
    private final boolean repeat;

    public KeyPressEvent(int key, boolean isRepeat) {
        this.key = key;
        repeat = isRepeat;
    }

    public int getKey() {
        return key;
    }

    public boolean isRepeat() {
        return repeat;
    }
}
