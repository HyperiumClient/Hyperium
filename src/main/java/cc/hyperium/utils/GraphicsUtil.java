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

package cc.hyperium.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Sk1er
 */
public class GraphicsUtil {
    public static GraphicsUtil INSTANCE = new GraphicsUtil();

    private GraphicsUtil() {
    }

    public Color transitionOfHueRange(double percentage, int startHue, int endHue, float saturation, float lightness) {
        double hue = ((percentage * (endHue - startHue)) + startHue) / 360;
        return hslColorToRgb(hue, saturation, lightness);
    }

    public Color transitionOfHueRange(double percentage, int startHue, int endHue) {
        return transitionOfHueRange(percentage, startHue, endHue, 1.0F, 0.5F);
    }

    private Color hslColorToRgb(double hue, double saturation, double lightness) {
        if (saturation == 0.0) {
            int grey = percToColor(lightness);
            return new Color(grey, grey, grey);
        }

        double q;
        if (lightness < 0.5) {
            q = lightness * (1 + saturation);
        } else {
            q = lightness + saturation - lightness * saturation;
        }
        double p = 2 * lightness - q;

        double oneThird = 1.0 / 3;
        double red = percToColor(hueToRgb(p, q, hue + oneThird));
        double green = percToColor(hueToRgb(p, q, hue));
        double blue = percToColor(hueToRgb(p, q, hue - oneThird));

        return new Color((int) red, (int) green, (int) blue);
    }

    private double hueToRgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;

        return t < 1.0 / 6 ? p + (q - p) * 6 * t : t < 1.0 / 2 ? q : t < 2.0 / 3 ? p + (q - p) * (2.0 / 3 - t) * 6 : p;
    }

    private int percToColor(double percentage) {
        return (int) Math.round(percentage * 255);
    }

}
