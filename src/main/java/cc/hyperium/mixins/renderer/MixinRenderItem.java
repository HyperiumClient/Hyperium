/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
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

package cc.hyperium.mixins.renderer;

import cc.hyperium.gui.settings.items.GeneralSetting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * A Mixin to the RenderItem class to provide ShinyPots support, not to be confused with the
 * ItemRenderer class, this class is entirely different
 *
 * @author boomboompower
 */
@Mixin(RenderItem.class)
public abstract class MixinRenderItem implements IResourceManagerReloadListener {
    
    /**
     * A shadow of the item enchantment texture
     */
    @Shadow
    @Final
    private static ResourceLocation RES_ITEM_GLINT;
    
    /**
     * A shadow of the model mesh field
     */
    @Shadow
    @Final
    private ItemModelMesher itemModelMesher;
    
    /**
     * A shadow of the texture manager field
     */
    @Shadow
    @Final
    private TextureManager textureManager;
    
    /**
     * Overrides the normal method to use our custom one
     *
     * @param stack the item to render
     * @param model the model of the item
     * @author boomboompower
     */
    @Overwrite
    public void renderItem(ItemStack stack, IBakedModel model) {
        this.renderItem(stack, model, false);
    }
    
    /**
     * A custom method which includes a "isInv" parameter, this specifies if the item being rendered
     * is in an inventory
     *
     * @param stack the item we are rendering
     * @param model the model of the item we will use
     * @param isInv true if the item is being rendered in an inventory
     * @author boomboompower
     */
    public void renderItem(ItemStack stack, IBakedModel model, boolean isInv) {
        if (!GeneralSetting.shinyPotsEnabled) {
            isInv = false;
        }
        
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            
            if (model.isBuiltInRenderer()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                TileEntityItemStackRenderer.instance.renderByItem(stack);
            } else {
                // Didn't really want to do this, but it's necessary
                boolean renderedAsPotion = false;
                
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    
                // Basically we want to render our potion effect before
                // the item is renderer so it doesn't render over the item
                if (isInv && stack.getItem() != null && stack.getItem() instanceof ItemPotion) {
                    this.renderPot(model); // Use our renderer instead of the normal one
                    
                    renderedAsPotion = true;
                }
                
                // Normal item renderer
                this.renderModel(model, stack);
                
                // If the effect has not been renderer as a potion, we'll use a normal render
                if (!renderedAsPotion && stack.hasEffect()) {
                    this.renderEffect(model); // The regular effect renderer
                }
            }
            
            GlStateManager.popMatrix();
        }
    }
    
    /**
     * Overrides the normal gui renderer to use our custom renderer instead
     *
     * @param stack the item to render
     * @param x the x location of the item
     * @param y the y location of the item
     * @author boomboompower
     */
    @Overwrite
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        GlStateManager.pushMatrix();
        this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
        this.textureManager.getTexture(TextureMap.locationBlocksTexture)
            .setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms()
            .applyTransform(ItemCameraTransforms.TransformType.GUI);
        
        this.renderItem(stack, ibakedmodel, true); // Changed to true because this IS an inventory
        
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
        this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }
    
    /**
     * Normal code for rendering an effect/enchantment on an item
     *
     * @param model the model of the item
     */
    private void renderEffect(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
    }
    
    /**
     * Basically the same as the above method, but does not include the depth code
     *
     * @param model the model
     */
    private void renderPot(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        
        this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
    }
    
    /**
     * A shadow method, this will call the method in the the class we are modifying
     */
    @Shadow
    protected abstract void renderModel(IBakedModel model, int color);
    
    /**
     * A shadow method, this will call the method in the the class we are modifying
     */
    @Shadow
    protected abstract void renderModel(IBakedModel model, ItemStack stack);
    
    /**
     * A shadow method, this will call the method in the the class we are modifying
     */
    @Shadow
    protected abstract void setupGuiTransform(int xPosition, int yPosition,
        boolean isGui3d);
}

