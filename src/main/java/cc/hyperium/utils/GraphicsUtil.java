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

package cc.hyperium.utils;

import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Sk1er
 */
public class GraphicsUtil {
    public static GraphicsUtil INSTANCE = new GraphicsUtil();
    private Graphics persistantGraphics;

    private GraphicsUtil() {

    }

    public CachedString generate(String text, int color, boolean shadow) {
        if (persistantGraphics == null)
            createGraphics();
        text = EnumChatFormatting.getTextWithoutFormattingCodes(text);
        int width = persistantGraphics.getFontMetrics().stringWidth(text);
        int height = 40;
        BufferedImage image = new BufferedImage(width*2, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(new Color(color));
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setFont(new Font("Arial", Font.PLAIN, height/2));
        graphics.drawString(text, 1, height/4);

//        System.out.println(graphics.getFont());

        CachedString cachedString = new CachedString(image);
        cachedString.setWidth(image.getWidth());
        cachedString.setHeight(height);
        cachedString.setReturnThing(width*2);
        return cachedString;
    }

    private void createGraphics() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        persistantGraphics = image.getGraphics();
    }

}
