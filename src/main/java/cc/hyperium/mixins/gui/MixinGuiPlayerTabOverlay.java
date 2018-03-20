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

package cc.hyperium.mixins.gui;

import cc.hyperium.gui.settings.items.GeneralSetting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay extends Gui {
    
    @Shadow @Final private Minecraft mc;
    
    /**
     * Allows the user to use numbered ping if they wish
     *
     * @author boomboompower
     */
    @Overwrite
    protected void drawPing(int p_175245_1_, int p_175245_2_, int yIn, NetworkPlayerInfo networkPlayerInfoIn) {
        final int ping = networkPlayerInfoIn.getResponseTime();
        
        if (GeneralSetting.numberPingEnabled) {
            int colour;
            
            if (ping > 500) {
                colour = 11141120;
            } else if (ping > 300) {
                colour = 11184640;
            } else if (ping > 200) {
                colour = 11193344;
            } else if (ping > 135) {
                colour = 2128640;
            } else if (ping > 70) {
                colour = 39168;
            } else if (ping >= 0) {
                colour = 47872;
            } else {
                colour = 11141120;
            }
            
            if (ping >= 0 && ping < 10000) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                final int x = p_175245_2_ + p_175245_1_ - (this.mc.fontRendererObj.getStringWidth(ping + "") >> 1) - 2;
                final int y = yIn + (this.mc.fontRendererObj.FONT_HEIGHT >> 2);
                this.mc.fontRendererObj.drawString(ping + "", (2 * x), (2 * y), colour);
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.popMatrix();
            }
            return;
        }
    
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        int i = 0;
        int j = 0;
        
        if (ping < 0) {
            j = 5;
        } else if (ping < 150) {
            j = 0;
        } else if (ping < 300) {
            j = 1;
        } else if (ping < 600) {
            j = 2;
        } else if (ping < 1000) {
            j = 3;
        } else {
            j = 4;
        }
        
        this.zLevel += 100.0F;
        drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, yIn, i * 10, 176 + j * 8, 10, 8);
        this.zLevel -= 100.0F;
    }
}
