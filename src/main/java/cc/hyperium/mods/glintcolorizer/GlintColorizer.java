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

package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;

import java.awt.*;

public class GlintColorizer extends AbstractMod {

    private Colors colors = new Colors();

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(colors);
        Hyperium.CONFIG.register(this);
        EventBus.INSTANCE.register(this);
        Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
        return this;
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        if (!Colors.enabled) {
            if (Colors.onepoint8glintcolorI != -8372020) Colors.onepoint8glintcolorI = -8372020;
            return;
        }
        if (Colors.chroma) {
            Colors.onepoint8glintcolorI = Color.HSBtoRGB(System.currentTimeMillis() %
                (10000L / Colors.chromaSpeed) / (10000.0f / Colors.chromaSpeed), 0.8f, 0.8f);
            return;
        }

        Colors.onepoint8glintcolorI = getIntFromColor(Colors.glintR, Colors.glintG, Colors.glintB);
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Glint Colorizer", "1.0", "powns");
    }

    public Colors getColors() {
        return colors;
    }

    public int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
