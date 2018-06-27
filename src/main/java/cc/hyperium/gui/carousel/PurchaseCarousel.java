package cc.hyperium.gui.carousel;

import cc.hyperium.gui.GuiBlock;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class PurchaseCarousel {

    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 60);
    private int index;
    private CarouselItem[] items;
    private List<GuiBlock> leftActions = new ArrayList<>();
    private List<GuiBlock> rightActions = new ArrayList<>();
    private SimpleAnimValue slideAnim = new SimpleAnimValue((long) 1, 0, 1);

    private PurchaseCarousel(int index, CarouselItem[] items) {
        if (items.length == 0)
            throw new IllegalArgumentException("Items must have at least 1 item in it");
        this.index = index;
        this.items = items;
    }

    public static PurchaseCarousel create(int index, CarouselItem... items) {
        return new PurchaseCarousel(index, items);
    }

    public void mouseClicked(int x, int y) {
        slideAnim = new SimpleAnimValue(1000L, 0, 1.0F);
        for (GuiBlock left : leftActions) {
            if (left != null && x >= left.getLeft() && x <= left.getRight() && y >= left.getTop() && y <= left.getBottom()) {
                index--;
                if (index < 0)
                    index = items.length - 1;
                return;
            }
        }
        for (GuiBlock right : rightActions) {
            if (right != null && x >= right.getLeft() && x <= right.getRight() && y >= right.getTop() && y <= right.getBottom()) {
                index++;
                if (index > items.length - 1)
                    index = 0;
                return;
            }
        }
        for (CarouselItem item : items) {
            item.mouseClicked(x, y);
        }
    }

    public void rotateRight() {
        slideAnim = new SimpleAnimValue(500L, 0, 1);
    }

    public void rotateLeft() {

    }

    public void render(int centerX, int centerY) {
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
    }


}
