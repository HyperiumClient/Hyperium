package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.GuiItem;
import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import cc.hyperium.addons.customcrosshair.utils.SaveUtils;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModGuiScreen extends GuiScreen
{
    public List<GuiItem> itemList;
    public List<String> toolTip;

    public ModGuiScreen() {
        this.itemList = new ArrayList<GuiItem>();
        this.toolTip = new ArrayList<String>();
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.toolTip != null) {
            GuiGraphics.drawBorderedRectangle(mouseX + 5, mouseY + 5, mouseX + this.getMaxWidth() + 9, mouseY + this.toolTip.size() * 11 + 8, GuiTheme.PRIMARY, GuiTheme.SECONDARY);
            for (int i = 0; i < this.toolTip.size(); ++i) {
                GuiGraphics.drawString(this.toolTip.get(i), mouseX + 8, mouseY + i * 11 + 9, 16777215);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void onGuiClosed() {
        SaveUtils.saveCurrentCrosshair(CustomCrosshairAddon.getCrosshairMod());
        super.onGuiClosed();
    }

    private int getMaxWidth() {
        int max = 0;
        for (int i = 0; i < this.toolTip.size(); ++i) {
            final int currentWidth = GuiGraphics.getStringWidth(this.toolTip.get(i));
            if (currentWidth > max) {
                max = currentWidth;
            }
        }
        return max;
    }
}

