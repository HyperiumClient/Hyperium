package cc.hyperium.gui.main.tabs;

import cc.hyperium.config.Settings;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.WrappingSettingItem;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.lang.reflect.Field;

/*
 * Created by Cubxity on 20/05/2018
 */
public class SettingsTab extends AbstractTab {
    private static HyperiumOverlay general = new HyperiumOverlay();
    private static HyperiumOverlay integrations = new HyperiumOverlay();
    private static HyperiumOverlay improvements = new HyperiumOverlay();

    private static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;

    static {
        for (Field f : Settings.class.getFields()) {
            ToggleSetting a = f.getAnnotation(ToggleSetting.class);
            if (a != null) {
                switch (a.category()) {
                    case GENERAL:
                        general.addToggle(a.name(), f);
                        break;
                    case IMPROVEMENTS:
                        improvements.addToggle(a.name(), f);
                        break;
                    case INTEGRATIONS:
                        integrations.addToggle(a.name(), f);
                        break;
                }
            }
        }
    }

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new WrappingSettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(general), Icons.SETTINGS.getResource(), "General", "General settings for Hyperium", "click to configure", 0, 0));

        items.add(new WrappingSettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(integrations), Icons.EXTENSION.getResource(), "Integrations", "Hyperium integrations", "Click to configure", 1, 0));

        items.add(new WrappingSettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(improvements), Icons.TOOL.getResource(), "Improvements", "Improvements and bug fixes", "Click to configure", 2, 0));
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
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY + offsetY, containerWidth, containerHeight);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        if (HyperiumMainGui.INSTANCE.getOverlay() != null) return;
        int i = Mouse.getEventDWheel();
        if (i < 0)
            offsetY -= 5;
        else if (i > 0)
            offsetY += 5;
    }
}
