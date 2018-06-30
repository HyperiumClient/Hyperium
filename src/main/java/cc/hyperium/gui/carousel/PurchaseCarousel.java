package cc.hyperium.gui.carousel;

import cc.hyperium.gui.Icons;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;
import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;
import java.awt.Font;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class PurchaseCarousel {

    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 60);
    private int index;
    private CarouselItem[] items;
    private SimpleAnimValue anim = new SimpleAnimValue((long) 1, 0, 1);
    private SimpleAnimValue larrow = new SimpleAnimValue(0L, 0.5f, 0.5f);
    private SimpleAnimValue rarrow = new SimpleAnimValue(0L, 0.5f, 0.5f);
    private boolean lhover = false;
    private boolean rhover = false;

    private PurchaseCarousel(int index, CarouselItem[] items) {
        if (items.length == 0)
            throw new IllegalArgumentException("Items must have at least 1 item in it");
        this.index = index;
        this.items = items;

        ScaledResolution current = ResolutionUtil.current();
        int totalWidth = current.getScaledWidth() / 3;
        int panel = totalWidth / 5;
    }

    public static PurchaseCarousel create(int index, CarouselItem... items) {
        return new PurchaseCarousel(index, items);
    }

    public void mouseClicked(int x, int y, int cx) {
        for (CarouselItem item : items) {
            item.mouseClicked(x, y);
        }

        ScaledResolution current = ResolutionUtil.current();
        int totalWidth = current.getScaledWidth() / 3;
        int panel = totalWidth / 5;
        int mainWidth = panel * 3;
        int cy = current.getScaledHeight() / 2;
        int lx = cx - mainWidth / 2;
        int rx = cx + mainWidth / 2;
        if (x >= lx - 8 && x <= lx && y >= cy - 5 && y <= cy + 5) {
            rotateLeft();
        } else if (x >= rx && x <= rx + 8 && y >= cy - 5 && y <= cy + 5) {
            rotateRight();
        }
    }

    public void rotateRight() {
        index++;
        if (index > items.length - 1)
            index = 0;
    }

    public void rotateLeft() {
        index--;
        if (index < 0)
            index = items.length - 1;
    }

    public void render(int centerX, int centerY, int mouseX, int mouseY) {
        ScaledResolution current = ResolutionUtil.current();
        int totalWidth = current.getScaledWidth() / 3;
        int panel = totalWidth / 5;
        int mainWidth = panel * 3;
        int sideHeight = current.getScaledHeight() / 5 * 2;
        int mainHeight = current.getScaledHeight() / 5 * 3;
        if (index > 0) {
            //Draw left side
            RenderUtils.drawSmoothRect(centerX - panel * 2, centerY - sideHeight / 2, centerX, centerY + sideHeight / 2, 4, new Color(23, 23, 23, 100).getRGB());
        }
        if (index < items.length - 1) {
            RenderUtils.drawSmoothRect(centerX, centerY - sideHeight / 2, centerX + panel * 2, centerY + sideHeight / 2, 4, new Color(23, 23, 23, 100).getRGB());
        }
        int objLeft = centerX - mainWidth / 2;
        int objBottom = centerY + mainHeight / 2;
        int objRight = centerX + mainWidth / 2;
        RenderUtils.drawSmoothRect(objLeft, centerY - mainHeight / 2, objRight, objBottom, 10, new Color(23, 23, 23, 255).getRGB());
        CarouselItem item = items[index];
        GlStateManager.scale(.5, .5, .5);

        int barHeight = 16;
        fr.drawString(item.getName(), (objLeft + 5) * 2, (objBottom - 50) * 2, Color.WHITE.getRGB());
        int purchaseRight = (objLeft + 50) * 2;
        RenderUtils.drawSmoothRect((objLeft + 5) * 2, (objBottom - 20) * 2, purchaseRight, (objBottom - 20 + barHeight) * 2, 10, Color.WHITE.getRGB());

        GlStateManager.scale(2 / 3F, 2 / 3F, 2 / 3F);
        fr.drawString(item.isPurchased() ? "Purchased" : "Purchase", (objLeft + 7) * 3, (objBottom - 18) * 3, new Color(23, 23, 23, 255).getRGB());
        GlStateManager.scale(3 / 2F, 3 / 2f, 3 / 2F);

        RenderUtils.drawFilledCircle(purchaseRight + barHeight * 2, (objBottom - 12) * 2, barHeight, Color.WHITE.getRGB());
        GlStateManager.color(0, 0, 0);
        Icons.DOWNLOAD.bind();
        Icons.SETTINGS.bind();
        Gui.drawScaledCustomSizeModalRect(purchaseRight + barHeight, (objBottom - 20) * 2, 0, 0, 144, 144, barHeight * 2, barHeight * 2, 144, 144);

        GlStateManager.scale(2.0, 2.0, 2.0);
        if (mouseX >= objLeft - 8 && mouseX <= objLeft && mouseY >= centerY - 5 && mouseY <= centerY + 5) {
            if (!lhover) {
                lhover = true;
                larrow = new SimpleAnimValue(500L, larrow.getValue(), 0.3f);
            }
        } else if (lhover) {
            lhover = false;
            larrow = new SimpleAnimValue(500L, larrow.getValue(), 0.5f);
        }
        float v = larrow.getValue();
        GlStateManager.scale(v, v, v);
        fr.drawCenteredString("<", (objLeft - 5) / v, centerY / v - 10, 0xffffff);
        GlStateManager.scale(1 / v, 1 / v, 1 / v);
        if (mouseX >= objRight && mouseX <= objRight + 8 && mouseY >= centerY - 5 && mouseY <= centerY + 5) {
            if (!rhover) {
                rhover = true;
                rarrow = new SimpleAnimValue(500L, rarrow.getValue(), 0.3f);
            }
        } else if (rhover) {
            rhover = false;
            rarrow = new SimpleAnimValue(500L, rarrow.getValue(), 0.5f);
        }
        v = rarrow.getValue();
        //System.out.println(v);
        GlStateManager.scale(v, v, v);
        fr.drawCenteredString(">", (objRight + 5) / v, centerY / v - 10, 0xffffff);
        GlStateManager.scale(1 / v, 1 / v, 1 / v);
    }


}
