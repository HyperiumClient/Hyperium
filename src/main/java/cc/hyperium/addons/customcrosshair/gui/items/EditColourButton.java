package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.gui.GuiEditColour;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class EditColourButton extends GuiItem
{
    private RGBA editColour;
    private Button editButton;

    public EditColourButton(final GuiScreen screen, final RGBA colour) {
        this(screen, -1, "noname", 0, 0, 0, 0, colour);
        this.editColour = colour;
    }

    public EditColourButton(final GuiScreen screen, final int id, final String text, final int x, final int y, final int width, final int height, final RGBA colour) {
        super(screen, id, text, x, y, width, height);
        this.editColour = colour;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        GuiGraphics.drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getHeight(), this.getPosY() + this.getHeight(), this.editColour, new RGBA(255, 255, 255, 255));
        this.editButton.drawItem(mouseX, mouseY);
        GuiGraphics.drawString(this.getDisplayText(), this.getPosX() + this.getHeight() + this.editButton.getWidth() + 4, this.getPosY() + this.getHeight() / 2 - 3, 16777215);
    }

    @Override
    public void setPosition(final int x, final int y) {
        super.setPosition(x, y);
        this.editButton = new Button(this.getCurrentScreen(), 0, "Edit", this.getPosX() + this.getHeight() + 2, this.getPosY() + this.getHeight() / 2 - 6, 25, 13);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (mouseX >= this.editButton.getPosX() && mouseX <= this.editButton.getPosX() + this.editButton.getWidth() && mouseY >= this.editButton.getPosY() && mouseY <= this.editButton.getPosY() + this.editButton.getHeight()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiEditColour(this.editColour, "Edit " + this.getDisplayText() + "..."));
        }
    }
}

