package cc.hyperium.gui.main.components;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.tabs.HomeTab;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Cubxity on 20/05/2018
 */
public abstract class AbstractTab {

    protected static HyperiumFontRenderer fr = HyperiumMainGui.INSTANCE.getFr();

    protected List<SettingItem> items = new ArrayList<>();
    protected List<SettingItem> witems = new ArrayList<>();
    protected int offsetY;

    public abstract void drawTabIcon();

    public abstract GuiBlock getBlock();

    public abstract void drawHighlight(float s);

    public void handleMouseInput() {
        if (HyperiumMainGui.INSTANCE.getOverlay() == null) {
            final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            int sw = sr.getScaledWidth();
            int sh = sr.getScaledHeight();
            final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;
            final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1;
            int pw = sr.getScaledWidth() / 15;
            if (pw > 144)
                pw = 144; // icon res
            int finalPw = pw;
            witems.forEach(s -> s.handleMouseInput(mx, my, sr.getScaledWidth() - finalPw, sr.getScaledWidth() - finalPw, finalPw * 2, finalPw));
            items.forEach(s -> s.handleMouseInput(mx, my, sr.getScaledWidth() - finalPw, sr.getScaledWidth() - finalPw, finalPw * 2, finalPw));


            // Scrolling.
            int i = Mouse.getEventDWheel();
            if (i > 0)
                offsetY += 1;
            else if (i < 0)
                offsetY -= 1;

            checkPos();
        }
    }

    private void checkPos() {
        if (this instanceof HomeTab)
            return;
        int max = 0;
        for (SettingItem item : items) {
            max = Math.max(max, item.getyIndex());
        }
        if (offsetY > 0)
            offsetY = 0;
        if (offsetY < -max)
            offsetY = -max;
    }

    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        items.stream().filter(i -> {
            int oty = topY + offsetY * (containerHeight / 3);
            int iy = oty + i.getyIndex() * (containerHeight / 3);
            return iy >= topY && topY + containerHeight >= iy + containerHeight / 3;
        }).forEach(i -> i.render(mouseX, mouseY, containerWidth, containerHeight, topX, topY + offsetY * (containerHeight / 3)));
        ScaledResolution current = ResolutionUtil.current();
        int i = current.getScaledWidth() / 30;
        int max = 0;

        for (SettingItem item : items) {
            max = Math.max(max, item.getyIndex());
        }
        if (offsetY < 0) {
            Icons.FA_UP_ARROW.bind();
            int x = current.getScaledWidth() / 2 - i / 2;
            int i5 = current.getScaledHeight() / 30;
            Gui.drawScaledCustomSizeModalRect(x, i5, 0, 0, 144, 144, i, i, 144, 144);
        }

        if (offsetY > -max) {
            Icons.FA_DOWN_ARROW.bind();
            int y1 = current.getScaledHeight() - current.getScaledHeight() / 30 - i;
            Gui.drawScaledCustomSizeModalRect(current.getScaledWidth() / 2 - i / 2, y1, 0, 0, 144, 144, i, i, 144, 144);
        }


    }

    public boolean isScrollable() { //TODO: Disable scroll
        return true;
    }

    public void mouseClicked(int x, int y) {
        int max = 0;

        for (SettingItem item : items) {
            max = Math.max(max, item.getyIndex());
        }
        ScaledResolution current = ResolutionUtil.current();
        int i = current.getScaledWidth() / 30;
        if (offsetY < 0) {
            int x2 = current.getScaledWidth() / 2 - i / 2;
            int i5 = current.getScaledHeight() / 30;
            GuiBlock block = new GuiBlock(x2, x2 + i5 * 2, i5, i5 + i5 * 2);
            if (block.isMouseOver(x, y)) {
                offsetY++;
                checkPos();
            }
        }
        if (offsetY > -max) {
            int x1 = current.getScaledWidth() / 2 - i / 2;
            int i5 = current.getScaledHeight() / 30;
            int y1 = current.getScaledHeight() - current.getScaledHeight() / 30 - i;
            GuiBlock block = new GuiBlock(x1, x1 + i5 * 2, y1, y1 + i5 * 2);
            if (block.isMouseOver(x, y)) {
                offsetY--;
                checkPos();
            }
        }
        items.forEach(settingItem -> settingItem.mouseClicked(x, y));
    }
}
