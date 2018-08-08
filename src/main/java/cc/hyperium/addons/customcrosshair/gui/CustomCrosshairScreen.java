package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.gui.items.CCGuiItem;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class CustomCrosshairScreen extends GuiScreen {

    public List<CCGuiItem> itemList = new ArrayList<>();
    public List<String> toolTip = new ArrayList<>();

    protected CustomCrosshairAddon addon;

    public CustomCrosshairScreen(CustomCrosshairAddon addon) {
        this.addon = addon;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.toolTip != null) {
            CustomCrosshairGraphics
                .drawBorderedRectangle(mouseX + 5, mouseY + 5, mouseX + this.getMaxWidth() + 9, mouseY + this.toolTip.size() * 11 + 8, CustomCrosshairAddon.PRIMARY, CustomCrosshairAddon.SECONDARY);
            for (int i = 0; i < this.toolTip.size(); ++i) {
                CustomCrosshairGraphics
                    .drawString(this.toolTip.get(i), mouseX + 8, mouseY + i * 11 + 9, 16777215);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        this.addon.getConfig().saveCurrentCrosshair();
    }

    private int getMaxWidth() {
        int max = 0;
        for (String aToolTip : this.toolTip) {
            final int currentWidth = this.fontRendererObj.getStringWidth(aToolTip);
            if (currentWidth > max) {
                max = currentWidth;
            }
        }
        return max;
    }
}

