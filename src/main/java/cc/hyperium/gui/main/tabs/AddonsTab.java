package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.OverlayToggle;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by Cubxity on 29/05/2018
 */
public class AddonsTab extends AbstractTab {
    private static int offsetY = 0; // static so it saves the previous location
    private GuiBlock block;
    private int y, w;

    public AddonsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        int yi = 0, xi = 0;
        for (AddonManifest a : AddonBootstrap.INSTANCE.getAddonManifests()) {
            Matcher m = Pattern.compile("B(\\d{2})").matcher("1.0 B11");
            m.find();
            items.add(new SettingItem(() -> {
                HyperiumOverlay overlay = new HyperiumOverlay();
                overlay.getComponents().add(new OverlayToggle(m.group(), false, b -> {
                }));
                HyperiumMainGui.INSTANCE.setOverlay(overlay);
            }, Icons.SETTINGS.getResource(), "Test", "A description", "A hover test", 0, 0));
            items.add(new SettingItem(() -> {
                if (a.getOverlay() != null) {
                    // While loading it has been checked so we don't have to do that here
                    try {
                        Class<?> clazz = Class.forName(a.getOverlay());
                        HyperiumOverlay overlay = (HyperiumOverlay) clazz.newInstance();
                        HyperiumMainGui.INSTANCE.setOverlay(overlay);
                    } catch (Exception e) {
                        HyperiumMainGui.Alert alert = new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {}, "Failed to load addon's config overlay");
                        HyperiumMainGui.getAlerts().add(alert);
                        e.printStackTrace(); // in case the check went wrong
                    }
                }
            }, Icons.EXTENSION.getResource(), a.getName(), "", "Configure addon", xi, yi));
            if (xi == 3) {
                xi = 0;
                yi++;
            } else
                xi++;
        }
    }

    @Override
    public void drawTabIcon() {
        Icons.EXTENSION.bind();
        Gui.drawScaledCustomSizeModalRect(5, y + 5, 0, 0, 144, 144, w - 10, w - 10, 144, 144);
    }

    @Override
    public GuiBlock getBlock() {
        return block;
    }

    @Override
    public void drawHighlight() {
        Gui.drawRect(0, y, 3, y + w, Color.WHITE.getRGB());
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
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
