package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonIcon extends GuiButton
{
    private ResourceLocation icon;

    public int sprite;

    private float scale;

    public GuiButtonIcon(int buttonID, ResourceLocation icon, int xPos, int yPos, int sprite, float scale)
    {
        super(buttonID, xPos, yPos, 52, 52, "");
        this.icon = icon;
        this.sprite = sprite;
        this.scale = scale;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(icon);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            // todo if flag glcolour
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale);
            GlStateManager.enableBlend();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            this.drawTexturedModalRect(this.xPosition / scale, this.yPosition / scale, 52 * sprite, 0, this.width, this.height);
            GlStateManager.disableBlend();
            GL11.glPopMatrix();
        }
    }
}