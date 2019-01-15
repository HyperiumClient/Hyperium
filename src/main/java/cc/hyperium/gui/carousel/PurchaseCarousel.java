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
import net.minecraft.client.resources.I18n;

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
    private GuiBlock activeBlock;

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

    public CarouselItem[] getItems() {
        return items;
    }

    public CarouselItem getCurrent() {
        return getItems()[index];
    }

    public void mouseClicked(int x, int y, int centerX) {

        ScaledResolution current = ResolutionUtil.current();
        int totalWidth = current.getScaledWidth() / 3;
        int panel = totalWidth / 5;
        int mainWidth = panel * 3;
        int centerY = current.getScaledHeight() / 2;
        int lx = centerX - mainWidth / 2;
        int rx = centerX + mainWidth / 2;
        if (x >= lx - 8 && x <= lx && y >= centerY - 5 && y <= centerY + 5) {
            rotateLeft();
        } else if (x >= rx && x <= rx + 8 && y >= centerY - 5 && y <= centerY + 5) {
            rotateRight();
        }
        int objLeft = centerX - mainWidth / 2;
        int purchaseLeft = objLeft + 5;
        int purchaseRight = (objLeft + 50);
        int mainHeight = current.getScaledHeight() / 5 * 3;
        int objBottom = centerY + mainHeight / 2;

        int barHeight = 16;

        int purchaseTop = objBottom - 20;
        int purchaseBottom = purchaseTop + barHeight;

        if (x >= purchaseLeft && x <= purchaseRight && y <= purchaseBottom && y >= purchaseTop) {
            getCurrent().getOnPurchase().accept(getCurrent());
        }
        int settingsLeft = purchaseRight + barHeight / 2;
        int settingsRight = settingsLeft + barHeight;

        if (x >= settingsLeft && x <= settingsRight && y <= purchaseBottom && y >= purchaseTop) {
            getCurrent().getOnSettingsClick().accept(getCurrent());
        }
        if (activeBlock != null && activeBlock.isMouseOver(x, y)) {
            getCurrent().getOnActivate().accept(getCurrent());
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
        activeBlock = null;
        ScaledResolution current = ResolutionUtil.current();
        int totalWidth = current.getScaledWidth() / 3;
        int panel = totalWidth / 5;
        int mainWidth = panel * 3;
        int sideHeight = current.getScaledHeight() / 5 * 2;
        int mainHeight = current.getScaledHeight() / 5 * 3;
        int objLeft = centerX - mainWidth / 2;
        int objBottom = centerY + mainHeight / 2;
        int objRight = centerX + mainWidth / 2;

        if (index > 0) {
            //Draw left side
            RenderUtils.drawSmoothRect(centerX - panel * 2, centerY - sideHeight / 2, centerX, centerY + sideHeight / 2, 4, new Color(23, 23, 23, 100).getRGB());
        }
        if (index < items.length - 1) {
            RenderUtils.drawSmoothRect(centerX, centerY - sideHeight / 2, centerX + panel * 2, centerY + sideHeight / 2, 4, new Color(23, 23, 23, 100).getRGB());
        }

        RenderUtils.drawSmoothRect(objLeft, centerY - mainHeight / 2, objRight, objBottom, 10, new Color(23, 23, 23, 255).getRGB());
        CarouselItem item = items[index];
        GlStateManager.scale(.5, .5, .5);

        int barHeight = 16;
        fr.drawString(item.getName(), (objLeft + 5) * 2, (objBottom - 50) * 2, Color.WHITE.getRGB());
        int purchaseRight = (objLeft + 50) * 2;
        RenderUtils.drawSmoothRect((objLeft + 5) * 2, (objBottom - 20) * 2, purchaseRight, (objBottom - 20 + barHeight) * 2, 10, Color.WHITE.getRGB());

        GlStateManager.scale(2 / 3F, 2 / 3F, 2 / 3F);
        fr.drawString(item.isPurchased() ? I18n.format("gui.purchase.purchased") : I18n.format("gui.purchase.purchase"), (objLeft + 5) * 3, (objBottom - 18) * 3, new Color(23, 23, 23, 255).getRGB());
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
        GlStateManager.scale(v, v, v);
        fr.drawCenteredString(">", (objRight + 5) / v, centerY / v - 10, 0xffffff);
        GlStateManager.scale(1 / v, 1 / v, 1 / v);
        String s = I18n.format("gui.purchase.state") + ": " + (getCurrent().isPurchased() ? (getCurrent().isActive() ? I18n.format("gui.purchase.active") : I18n.format("gui.purchase.inactive")) : I18n.format("gui.purchase.notpurchased"));
        float e = .5F;
        GlStateManager.scale(e, e, e);
        fr.drawString(s, (centerX - fr.getWidth(s) / 4) / e, (centerY - mainHeight / 2 + 15) / e, Color.GREEN.getRGB());
        if (getCurrent().isPurchased() && !getCurrent().isActive()) {
            s = I18n.format("gui.purchase.clicktouse");
            float width = fr.getWidth(s);
            float x = centerX - width / 4;
            int i = centerY - mainHeight / 2 + 35;
            activeBlock = new GuiBlock((int) x, (int) (x + width * 2), i, i + 10);

            fr.drawString(s, x / e, i / e, Color.GREEN.getRGB());
        }
        GlStateManager.scale(1 / e, 1 / e, 1 / e);
    }


}
