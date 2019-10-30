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

package cc.hyperium.mods.chromahud.displayitems.chromahud;


import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sk1er
 */
public class PotionEffects extends DisplayItem {

    private boolean potionIcon;

    public PotionEffects(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
        potionIcon = raw.optBoolean("potionIcon");
    }

    public void draw(int x, double y, boolean isConfig) {
        int row = 0;
        double scale = ElementRenderer.getCurrentScale();
        Collection<PotionEffect> effects = new ArrayList<>();

        if (isConfig) {
            effects.add(new PotionEffect(1, 100, 1));
            effects.add(new PotionEffect(3, 100, 2));
        } else {
            effects = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
        }

        List<String> tmp = new ArrayList<>();

        for (PotionEffect potioneffect : effects) {
            Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

            if (potionIcon) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));

                if (potion.hasStatusIcon()) {
                    int potionStatusIconIndex = potion.getStatusIconIndex();
                    drawTexturedModalRect(!ElementRenderer.getCurrent().isRightSided() ? (int) (x / scale) - 20 :
                            (int) (x / scale), (int) ((y + row * 16)) - 4, potionStatusIconIndex % 8 * 18,
                        198 + potionStatusIconIndex / 8 * 18, 18, 18);
                }
            }

            StringBuilder s1 = new StringBuilder(I18n.format(potion.getName()));

            switch (potioneffect.getAmplifier()) {
                case 1:
                    s1.append(" ").append(I18n.format("enchantment.level.2"));
                    break;
                case 2:
                    s1.append(" ").append(I18n.format("enchantment.level.3"));
                    break;
                case 3:
                    s1.append(" ").append(I18n.format("enchantment.level.4"));
                    break;
            }

            String s = Potion.getDurationString(potioneffect);
            String text = s1 + " - " + s;
            tmp.add(text);
            ElementRenderer.draw((int) (x / scale), ((y + row * 16)), text);
            row++;
        }

        width = isConfig ? ElementRenderer.maxWidth(tmp) : 0;
        height = row * 16;
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex((float) (textureX) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex((float) (textureX + width) * f, (float) (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((float) (textureX + width) * f, (float) (textureY) * f1).endVertex();
        worldrenderer.pos(x, y, 0).tex((float) (textureX) * f, (float) (textureY) * f1).endVertex();
        tessellator.draw();
    }

    public void togglePotionIcon() {
        potionIcon = !potionIcon;
    }

    @Override
    public void save() {
        data.put("potionIcon", potionIcon);
    }

    @Override
    public String toString() {
        return "PotionEffects{" +
            "potionIcon=" + potionIcon +
            '}';
    }
}
