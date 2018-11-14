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

package cc.hyperium.event;

/**
 * Invoked once a key is pressed
 */
public class KeypressEvent extends Event {

    private final int key;
    private final boolean repeat;

    public KeypressEvent(int key, boolean isRepeat) {
        this.key = key;
        this.repeat = isRepeat;
    }

    public int getKey() {
        return this.key;
    }

    public boolean isRepeat() {
        return this.repeat;
    }
}
