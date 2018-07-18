package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.gui.ModGuiScreen;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class HelpButton extends GuiItem
{
    public HelpButton(final GuiScreen screen, final List<String> text) {
        super(screen, -1, "", 0, 0, 10, 10);
        this.helpText = text;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        GuiGraphics.drawThemeBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        GuiGraphics.drawString("?", this.getPosX() + 3, this.getPosY() + 2, 16777215);
        if (mouseX >= this.getPosX() && mouseX <= this.getPosX() + this.getWidth() && mouseY >= this.getPosY() && mouseY <= this.getPosY() + this.getHeight() && this.getCurrentScreen() instanceof ModGuiScreen) {
            ((ModGuiScreen)this.getCurrentScreen()).toolTip = this.getHelpText();
        }
    }
}
