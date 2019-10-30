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

package cc.hyperium.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlayLabel;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.mods.glintcolorizer.Colors;

import java.lang.reflect.Field;

public class ColourOptions extends HyperiumOverlay {

    @ConfigOpt
    public static int accent_r = 136;
    @ConfigOpt
    public static int accent_g = 255;
    @ConfigOpt
    public static int accent_b;


    public static boolean toggle = true;

    public ColourOptions() {
        super("GUI Options", false);
        reload();
    }

    /*
     * TAKEN FROM GLINTCOLORIZERSETTINGS CLASS as OverlaySlider didn't work (and i wasnt bothered to do all of that code)
     */
    private void addSlider(String label, Field f, int max, int min, boolean updateColor) {
        f.setAccessible(true);
        try {
            getComponents().add(new OverlaySlider(label, min, max, ((Integer) f.get(null)).floatValue(), (i) -> {
                try {
                    f.set(null, i.intValue());
                    Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void addLabel(String text) {
        getComponents().add(new OverlayLabel(text, true, () -> {
        }));
    }

    private void reload() {
        try {
            addLabel("Accent Colour:");
            addSlider("Red", getClass().getField("accent_r"), 255, 0, true);
            addSlider("Green", getClass().getField("accent_g"), 255, 0, true);
            addSlider("Blue", getClass().getField("accent_b"), 255, 0, true);
            addToggle("Example: ", getClass().getField("toggle"), null, true, this);
            getComponents().add(new OverlayButton("Reset to default colours", () -> {
                try {
                    getClass().getField("accent_r").setInt(null, 136);
                    getClass().getField("accent_g").setInt(null, 255);
                    getClass().getField("accent_b").setInt(null, 0);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

