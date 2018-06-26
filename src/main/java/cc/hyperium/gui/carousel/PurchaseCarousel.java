package cc.hyperium.gui.carousel;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class PurchaseCarousel {

    private final HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    private int index;
    private CarouselItem[] items;
    private List<GuiBlock> leftActions = new ArrayList<>();
    private List<GuiBlock> rightActions = new ArrayList<>();

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
        fr.drawString(item.getName(), objLeft + 5, objBottom - 30, Color.WHITE.getRGB());

        RenderUtils.drawSmoothRect(objLeft + 5, objBottom - 15, objLeft + 40, objBottom - 5, 3, Color.WHITE.getRGB());
        GlStateManager.scale(2 / 3F, 2 / 3F, 2 / 3F);
        fr.drawString(item.isPurchased() ? "Purchased" : "Purchase", (objLeft + 7) * 3 / 2, (objBottom - 15) * 3 / 2, new Color(23, 23, 23, 255).getRGB());
        GlStateManager.scale(3 / 2F, 3 / 2f, 3 / 2F);

        Icons.SETTINGS.bind();
        Gui.drawScaledCustomSizeModalRect(objRight - 40, objBottom - 16, 0, 0, 144, 144, 10, 10, 144, 144);

    }


}
