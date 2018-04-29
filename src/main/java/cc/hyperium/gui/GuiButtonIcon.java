/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonIcon extends GuiButton {
    public final int sprite;
    private final ResourceLocation icon;
    private boolean outline;
    private final float scale;

    public GuiButtonIcon(int buttonID, ResourceLocation icon, int xPos, int yPos, int sprite, float scale) {
        super(buttonID, xPos, yPos, 52, 52, "");
        this.icon = icon;
        this.sprite = sprite;
        this.scale = scale;
    }

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(icon);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            // todo if flag glcolour
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale);
            GlStateManager.enableBlend();
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTranslatef(this.xPosition / scale + ((float) width) / scale, this.yPosition / scale + ((float) height) / scale, 0);
            GlStateManager.color(0, 0, 0, 1.0F);
            float mag = 1.25F;
            GlStateManager.scale(mag, mag, mag);
            this.drawTexturedModalRect(-width / 2, -height / 2, 52 * sprite, 0, this.width, this.height);
            GlStateManager.scale(1.0F / mag, 1.0F / mag, 1.0F / mag);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(-width / 2, -height / 2, 52 * sprite, 0, this.width, this.height);

            GlStateManager.disableBlend();
            GL11.glPopMatrix();
        }
    }
}