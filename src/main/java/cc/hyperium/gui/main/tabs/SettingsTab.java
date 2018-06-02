package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlayToggle;
import cc.hyperium.gui.main.components.SettingItem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    public static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new SettingItem(() -> {
            HyperiumOverlay overlay = new HyperiumOverlay();
            overlay.getComponents().add(new OverlayToggle("A test toggle", false, b -> {
            }));
            HyperiumMainGui.INSTANCE.setOverlay(overlay);
        }, Icons.SETTINGS.getResource(), "Test", "A description", "A hover test", 0, 0));
    }

    @Override
    public void drawTabIcon() {
        Icons.SETTINGS.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {
        GlStateManager.disableBlend();
        Gui.drawRect(0, y, 3, y + w, new Color(255, 255, 255, 100).getRGB());
        GlStateManager.enableBlend();
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY + offsetY, containerWidth, containerHeight);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0)
            offsetY -= 5;
        else if (i > 0)
            offsetY += 5;
    }
}
