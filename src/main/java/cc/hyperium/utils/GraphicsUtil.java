package cc.hyperium.utils;

import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by mitchellkatz on 3/9/18. Designed for production use on Sk1er.club
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
