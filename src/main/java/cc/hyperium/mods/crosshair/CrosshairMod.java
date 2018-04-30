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

package cc.hyperium.mods.crosshair;

import cc.hyperium.event.RenderGuiEvent;

public class CrosshairMod {

    private static CrosshairMod instance;

//    private CrosshairType crosshair = new Crosshair();

    public static CrosshairMod getInstance() {
        if (instance == null)
            instance = new CrosshairMod();
        return instance;
    }

    // todo: @InvokeEvent do crosshair shit when u can cba - note to self, kevin :*
    public void renderEvent(RenderGuiEvent event) {
        // draw whatever my dudes
//        crosshair.draw(true, Color.BLACK, 10, 10, 10, 10, 10);
    }
}
