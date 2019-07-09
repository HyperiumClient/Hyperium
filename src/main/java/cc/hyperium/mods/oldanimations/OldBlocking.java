package cc.hyperium.mods.oldanimations;

import cc.hyperium.config.Settings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class OldBlocking {

    /**
     * May require additional cleanups, however it's fine for the moment.
     */
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, RendererLivingEntity<?> livingEntityRenderer) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();

        if (itemstack != null) {
            GlStateManager.pushMatrix();
            if (livingEntityRenderer.getMainModel().isChild) {
                float f = 0.5f;
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.scale(f, f, f);
            }
            Label_0327:
            if (entitylivingbaseIn instanceof EntityPlayer) {
                if (Settings.OLD_BLOCKING) {
                    if (((EntityPlayer) entitylivingbaseIn).isBlocking()) {
                        if (entitylivingbaseIn.isSneaking()) {
                            ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                            GlStateManager.scale(1.05f, 1.05f, 1.05f);
                            GlStateManager.translate(-0.58f, 0.32f, -0.07f);
                            GlStateManager
                                .rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
                        } else {
                            ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                            GlStateManager.scale(1.05f, 1.05f, 1.05f);
                            GlStateManager.translate(-0.45f, 0.25f, -0.07f);
                            GlStateManager
                                .rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
                        }
                    } else {
                        ((ModelBiped) livingEntityRenderer.getMainModel())
                            .postRenderArm(0.0625f);
                    }
                } else {
                    ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
                }
                if (!Settings.OLD_ITEM_HELD) {
                    GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
                } else {
                    if (!((EntityPlayer) entitylivingbaseIn).isBlocking()) {
                        if (Settings.OLD_ITEM_HELD) {
                            GlStateManager.translate(-0.0855f, 0.4775f, 0.1585f);
                            GlStateManager.rotate(-19.0f, 20.0f, 0.0f, -6.0f);
                            break Label_0327;
                        }
                    }
                    if (((EntityPlayer) entitylivingbaseIn).isBlocking()) {
                        GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
                    }
                }
            } else {
                ((ModelBiped) livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            }
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer) entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                float f2 = 0.375f;
                GlStateManager.scale(-f2, -f2, f2);
            }
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.203125f, 0.0f);
            }
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }
}
