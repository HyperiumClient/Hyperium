/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.hyperium.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(ResourcePackListEntry.class)
public abstract class MixinGuiResourcePack {

    @Shadow
    @Final
    protected Minecraft mc;

    @Shadow
    protected abstract int func_183019_a();

    @Shadow
    protected abstract String func_148312_b();

    @Shadow
    protected abstract String func_148311_a();

    /**
     * Fixes Minecraft's shitty rendering code
     *
     * @author Kevin Brewster
     */
    @Inject(method = "drawEntry", at = @At("HEAD"), cancellable = true)
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
                          int mouseX, int mouseY, boolean isSelected, CallbackInfo ci) {
        boolean compact = true;
        if (compact) {
            ci.cancel();
            int i = this.func_183019_a();

            if (i != 1) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            String s = this.func_148312_b();
            String s1 = this.func_148311_a();

            int i1 = this.mc.fontRendererObj.getStringWidth(s);

            if (i1 > 157) {
                s = this.mc.fontRendererObj.trimStringToWidth(s, 157 - this.mc.fontRendererObj.getStringWidth("...")) + "...";
            }

            this.mc.fontRendererObj.drawStringWithShadow(s, (float) (x + 2), (float) (y + 4), 16777215);
            List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(s1, 157);

            GL11.glPushMatrix();
            GL11.glScalef(0.7F, 0.7F, 0.7F);

            StringBuilder msg = new StringBuilder();
            list.forEach(b -> msg.append(b).append(" "));

            double max = 157 / 0.7;
            int len = this.mc.fontRendererObj.getStringWidth(msg.toString());

            String info = msg.toString();
            if (len > max) {
                info = this.mc.fontRendererObj.trimStringToWidth(msg.toString(), (int) (max - this.mc.fontRendererObj.getStringWidth("..."))) + "...";
            }
            this.mc.fontRendererObj.drawStringWithShadow(info, (float) ((x + 5) / 0.7), (float) ((y + 6 + 10) / 0.7), 8421504);


            GL11.glPopMatrix();

            Gui.drawRect(x + 2, y + 12 + 17,
                    x + listWidth - 10, y + 12 + 18, new Color(94, 98, 94, 235).getRGB());


        }
    }
}
