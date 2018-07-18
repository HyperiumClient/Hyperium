package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import net.minecraft.client.gui.GuiScreen;

public class Tickbox extends GuiItem {
    private boolean checked;

    public Tickbox(final GuiScreen screen) {
        super(screen);
        this.setWidth(10);
        this.setHeight(10);
    }

    public Tickbox(final GuiScreen screen, final int id, final String displayText, final int posX, final int posY) {
        super(screen, id, displayText, posX, posY, 10, 10);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        this.toggleChecked();
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        GuiGraphics.drawThemeBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        GuiGraphics.drawString(this.getDisplayText(), this.getPosX() + this.getWidth() + 3, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
        if (this.getChecked()) {
            GuiGraphics.drawFilledRectangle(this.getPosX() + 2, this.getPosY() + 2, this.getPosX() + this.getWidth() - 1, this.getPosY() + this.getHeight() - 1, new RGBA(50, 255, 50, 255));
        }
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(final boolean newChecked) {
        this.checked = newChecked;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
