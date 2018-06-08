package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.HyperiumMainGui;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.AbstractTab;
import cc.hyperium.gui.main.components.SettingItem;
import cc.hyperium.installer.InstallerFrame;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.gui.Gui;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddonsInstallerTab extends AbstractTab {
    private static int offsetY = 0; // static so it saves the previous location
    private int y, w;
    private GuiBlock block;

    String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";

    public AddonsInstallerTab(int y, int w) {

        this.y = y;
        this.w = w;
        int yi = 0, xi = 0;

        List<JSONObject> ao = new ArrayList<>();
        JSONObject versionsJson = null;
        try {
            versionsJson = new JSONObject(InstallerFrame.get(versions_url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int current = 0;

        block = new GuiBlock(0, w, y, y + w);

        for (Object o : versionsJson.getJSONArray("addons")) {
            ao.add((JSONObject) o);
            items.add(new SettingItem(() -> {
                //stuff in here AMP MAKE THEM METHODS BOI
            }, Icons.DOWNLOAD.getResource(), ao.get(current).getString("name"), "Addon Description", "Download Addon", xi, yi));
            if (xi == 3) {
                xi = 0;
                yi++;
            } else
                xi++;
            current++;
        }
    }

    @Override
    public void drawTabIcon() {
        Icons.DOWNLOAD.bind();
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
        super.draw(mouseX, mouseY, topX, topY, containerWidth, containerHeight);
    }
}
