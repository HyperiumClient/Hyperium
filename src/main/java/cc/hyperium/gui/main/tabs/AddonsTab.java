package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

/*
 * Created by Cubxity on 29/05/2018
 */
public class AddonsTab extends AbstractTab {
    private GuiBlock block;
    private int y, w;

    public AddonsTab(int y, int w) {

        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;
        int yi = 0, xi = 0;
        for (AddonManifest a : AddonBootstrap.INSTANCE.getAddonManifests()) {
            items.add(new SettingItem(() -> {
                if (a.getOverlay() != null) {
                    // While loading it has been checked so we don't have to do that here
                    try {
                        Class<?> clazz = Class.forName(a.getOverlay());
                        HyperiumOverlay overlay = (HyperiumOverlay) clazz.newInstance();
                        HyperiumMainGui.INSTANCE.setOverlay(overlay);
                    } catch (Exception e) {
                        HyperiumMainGui.Alert alert = new HyperiumMainGui.Alert(Icons.ERROR.getResource(), () -> {
                        }, "Failed to load addon's config overlay");
                        HyperiumMainGui.INSTANCE.getAlerts().add(alert);
                        e.printStackTrace(); // in case the check went wrong
                    }
                }
            }, Icons.EXTENSION.getResource(), a.getName(), a.getDesc(), "Configure addon", xi, yi));
            if (xi == 2) {
                xi = 0;
                yi++;
            } else
                xi++;
        }
        items.add(new SettingItem(() -> HyperiumMainGui.INSTANCE.setTab(HyperiumMainGui.INSTANCE.getTabs().get(5)), Icons.DOWNLOAD.getResource(), "Addons", "Download addons", "Click to open menu to download addons", xi, yi));
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
    public void drawHighlight(float s) {
        Gui.drawRect(0, (int) (y + s * (s * w / 2)), 3, (int) (y + w - s * (w / 2)), 0xffffffff);
    }

    @Override
    public void draw(int mouseX, int mouseY, int topX, int topY, int containerWidth, int containerHeight) {
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
    }
}
