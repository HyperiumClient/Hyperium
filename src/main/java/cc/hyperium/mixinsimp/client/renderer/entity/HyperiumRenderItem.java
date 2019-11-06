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

package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.mixins.client.renderer.entity.IMixinRenderItem;
import cc.hyperium.mixins.client.renderer.entity.IMixinRenderItem2;
import cc.hyperium.mods.glintcolorizer.Colors;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HyperiumRenderItem {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private RenderItem parent;

    private final int MAX_EFFECTS = 25;
    private Cache<Integer, Integer> colorCache = Caffeine.newBuilder()
        .maximumSize(MAX_EFFECTS)
        .executor(Multithreading.POOL)
        .build();

    public HyperiumRenderItem(RenderItem parent) {
        this.parent = parent;
    }

    public void callHeadScale(ItemStack stack, boolean isInv, double scale) { // BigHead
        if (!isInv && stack.getItem() != null && stack.getItem() instanceof ItemSkull) {
            GlStateManager.scale(scale, scale, scale);
        }
    }

    public boolean renderShinyPot(ItemStack stack, IBakedModel model, boolean isInv) {
        boolean renderedAsPotion = false;
        if (Settings.SHINY_POTS && isInv && stack.getItem() != null && stack.getItem() instanceof ItemPotion) {
            int glintColor = getPotionColor(stack);
            renderPot(model, glintColor);

            renderedAsPotion = true;
        }
        return renderedAsPotion;
    }

    /**
     * Basically the same as renderEffect, but does not include the depth code and uses custom color
     *
     * @param model the model
     */
    private void renderPot(IBakedModel model, int color) {
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_COLOR, 1);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem2) parent).callRenderModel(model, color);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem2) parent).callRenderModel(model, color);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);

        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    private int getPotionColor(ItemStack item) {
        if (Settings.SHINY_POTS_MATCH_COLOR) {
            int potionId = item.getMetadata();

            Integer cached = colorCache.getIfPresent(potionId);

            if (cached != null) {
                return cached;
            } else {
                int color = Items.potionitem.getColorFromItemStack(item, 0) | 0xFF000000;
                colorCache.put(potionId, color);
                return color;
            }
        } else {
            return Colors.onepoint8glintcolorI;
        }
    }
}
