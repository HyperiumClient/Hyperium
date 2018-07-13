package cc.hyperium.gui.main.tabs;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.main.components.AbstractTab;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class ModsTab extends AbstractTab {
    private GuiBlock block;
    private int y, w;

    public ModsTab(int y, int w) {
        block = new GuiBlock(0, w, y, y + w);
        this.y = y;
        this.w = w;

        //TODO: Add buttons to view license in the licenses overlay

    }

    @Override
    public void drawTabIcon() {
        Icons.FA_WRENCH.bind();
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
    public String getTitle() {
        return "Mods";
    }

}
