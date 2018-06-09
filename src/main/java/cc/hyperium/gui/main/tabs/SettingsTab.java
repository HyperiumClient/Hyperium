package cc.hyperium.gui.main.tabs;

import cc.hyperium.config.Category;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.Settings;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlaySelector;
import cc.hyperium.gui.main.components.SettingItem;
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
    private static HyperiumOverlay cosmetics = new HyperiumOverlay();
    private static HyperiumOverlay spotify = new HyperiumOverlay();

    private static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;

    static {
        for (Field f : Settings.class.getFields()) {
            ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
            SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
            if (ts != null)
                getCategory(ts.category()).addToggle(ts.name(), f);
            else if (ss != null)
                try {
                    getCategory(ss.category()).getComponents().add(new OverlaySelector<>(ss.name(), String.valueOf(f.get(null)), si -> {
                        try {
                            f.set(null, si);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }, ss.items()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
        Settings.save();
    }

    public SettingsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(general), Icons.SETTINGS.getResource(), "General", "General settings for Hyperium", "click to configure", 0, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(integrations), Icons.EXTENSION.getResource(), "Integrations", "Hyperium integrations", "click to configure", 1, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(improvements), Icons.TOOL.getResource(), "Improvements", "Improvements and bug fixes", "click to configure", 2, 0));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(cosmetics), Icons.COSMETIC.getResource(), "Cosmetics", "Bling out your Minecraft Avatar", "click to configure", 0, 1));

        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setOverlay(spotify), Icons.SPOTIFY.getResource(), "Spotify", "Hyperium Spotify Settings", "click to configure", 1, 1));

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

    private static HyperiumOverlay getCategory(Category category) {
        switch (category) {
            case GENERAL:
                return general;
            case IMPROVEMENTS:
                return improvements;
            case INTEGRATIONS:
                return integrations;
            case COSMETICS:
                return cosmetics;
            case SPOTIFY:
                return spotify;
        }
        return general;
    }
}
