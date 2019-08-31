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
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
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

    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        IBakedModel ibakedmodel = parent.getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinRenderItem) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture)
            .setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ((IMixinRenderItem) parent).callSetupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms()
            .applyTransform(ItemCameraTransforms.TransformType.GUI);

        this.renderItem(stack, ibakedmodel, true); // Changed to true because this IS an inventory

        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinRenderItem) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    public void renderItem(ItemStack stack, IBakedModel model, boolean isInv) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            boolean isHead = !isInv && stack.getItem() != null && stack.getItem() instanceof ItemSkull;
            double headScale = Settings.HEAD_SCALE_FACTOR;

            if (model.isBuiltInRenderer()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();

                // BigHead implementation
                if (isHead) {
                    GlStateManager.scale(headScale, headScale, headScale);
                }

                TileEntityItemStackRenderer.instance.renderByItem(stack);

                // BigHead implementation
                if (isHead) {
                    GlStateManager.scale(1.0 / headScale, 1.0 / headScale, 1.0 / headScale);
                }
            } else {
                // Used to detect if the item has a already had an effect rendered
                boolean renderedAsPotion = false;

                GlStateManager.translate(-0.5F, -0.5F, -0.5F);

                // We want to render our potion effect before
                // the item is renderer so the effect doesn't obscure the item
                if (Settings.SHINY_POTS && isInv && stack.getItem() != null && stack.getItem() instanceof ItemPotion) {
                    int glintColor = getPotionColor(stack);
                    renderPot(model, glintColor);

                    renderedAsPotion = true;
                }

                // BigHead implementation
                if (isHead) {
                    GlStateManager.scale(headScale, headScale, headScale);
                }

                // Normal item renderer
                ((IMixinRenderItem) parent).callRenderModel(model, stack);

                // Prevent double-rendering of the items effects
                if (!renderedAsPotion && stack.hasEffect()) {
                    this.renderEffect(model); // Render the item with the normal effects
                }

                // BigHead implementation
                if (isHead) {
                    GlStateManager.scale(1.0 / headScale, 1.0 / headScale, 1.0 / headScale);
                }
            }

            GlStateManager.popMatrix();
        }
    }

    /**
     * Basically the same as the above method, but does not include the depth code
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

    /**
     * Normal code for rendering an effect/enchantment on an item
     *
     * @param model the model of the item
     */
    private void renderEffect(IBakedModel model) {
        if (Settings.DISABLE_ENCHANT_GLINT) {
            return;
        }
        GlStateManager.depthMask(false);

        GlStateManager.depthFunc(GL11.GL_EQUAL); // This is for render depth
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F; // Animates the effect
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem2) parent).callRenderModel(model, Colors.onepoint8glintcolorI);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem2) parent).callRenderModel(model, Colors.onepoint8glintcolorI);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();

        GlStateManager.depthFunc(GL11.GL_LEQUAL); // Changes back to the normal depth

        GlStateManager.depthMask(true);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    private int getPotionColor(ItemStack item) {
        if (Settings.SHINY_POTS_MATCH_COLOR) {
            int potionId = item.getMetadata();

            Integer cached = colorCache.getIfPresent(potionId);

            if (cached != null) return cached;
            else {
                int color = Items.potionitem.getColorFromItemStack(item, 0) | 0xFF000000;
                colorCache.put(potionId, color);
                return color;
            }
        } else {
            return Colors.onepoint8glintcolorI;
        }
    }
}
