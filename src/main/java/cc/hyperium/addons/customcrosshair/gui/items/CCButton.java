package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairGraphics;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class CCButton extends CCGuiItem {

    public CCButton(final GuiScreen screen) {
        super(screen);
    }

    public CCButton(final GuiScreen screen, final int id, final String text, final int x,
        final int y, final int width, final int height) {
        super(screen, id, text, x, y, width, height);
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        Color backgroundColour = CustomCrosshairAddon.PRIMARY_T;
        if (mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + this.getHeight()) { backgroundColour = new Color(255, 180, 0, 255);
        }
        CustomCrosshairGraphics
            .drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), backgroundColour, CustomCrosshairAddon.SECONDARY);
        CustomCrosshairGraphics
            .drawString(this.getDisplayText(), this.getPosX() + this.getWidth() / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.getDisplayText()) / 2 + 1, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
    }
}
