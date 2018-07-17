package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import net.minecraft.client.gui.GuiScreen;

public class Button extends GuiItem {
    public Button(final GuiScreen screen) {
        super(screen);
    }

    public Button(final GuiScreen screen, final int id, final String text, final int x, final int y, final int width, final int height) {
        super(screen, id, text, x, y, width, height);
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        RGBA backgroundColour = GuiTheme.PRIMARY_T;
        if (mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + this.getHeight()) {
            backgroundColour = new RGBA(255, 180, 0, 255);
        }
        GuiGraphics.drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), backgroundColour, GuiTheme.SECONDARY);
        GuiGraphics.drawString(this.getDisplayText(), this.getPosX() + this.getWidth() / 2 - GuiGraphics.getStringWidth(this.getDisplayText()) / 2 + 1, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
    }
}
