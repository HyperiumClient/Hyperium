package cc.hyperium.gui.main.components;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
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

    public abstract void drawTabIcon();

    public abstract GuiBlock getBlock();

    public abstract void drawHighlight(float s);
    private int yOffset;
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
        }
    }

    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        items.forEach(i -> i.render(mouseX, mouseY, containerWidth, containerHeight, topX, topY));
    }

    public boolean isScrollable() { //TODO: Disable scroll
        return true;
    }

    public void mouseClicked(int x, int y) {
        items.forEach(settingItem -> settingItem.mouseClicked(x, y));
    }
}
