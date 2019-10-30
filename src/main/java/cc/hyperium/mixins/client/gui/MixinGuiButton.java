/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@SuppressWarnings("unused")
@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {

    @Shadow public boolean visible;
    @Shadow protected boolean hovered;
    @Shadow public int xPosition;
    @Shadow public int yPosition;
    @Shadow protected int width;
    @Shadow protected int height;
    @Shadow protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);
    @Shadow public boolean enabled;
    @Shadow public String displayString;
    @Shadow @Final protected static ResourceLocation buttonTextures;

    private double hoverFade;
    private long prevDeltaTime;

    /**
     * @author asbyth
     * @reason Custom Hyperium buttons
     */
    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (prevDeltaTime == 0) prevDeltaTime = System.currentTimeMillis();

        if (visible) {
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            double hoverInc = (System.currentTimeMillis() - prevDeltaTime) / 2F;
            hoverFade = hovered ? Math.min(100, hoverFade + hoverInc) : Math.max(0, hoverInc - hoverInc);

            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(0, 0, 0, (int) (100 - (hoverFade / 2))).getRGB());
            mouseDragged(mc, mouseX, mouseY);

            int textColor = enabled ? 255 : 180;
            drawCenteredString(mc.fontRendererObj, displayString, xPosition + width / 2, yPosition + (height - 8) / 2,
                new Color(textColor, textColor, textColor, 255).getRGB());
            prevDeltaTime = System.currentTimeMillis();
        }
    }
}
