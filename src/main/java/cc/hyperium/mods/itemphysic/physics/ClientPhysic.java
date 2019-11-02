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

package cc.hyperium.mods.itemphysic.physics;

import cc.hyperium.mods.itemphysic.ItemDummyContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class ClientPhysic {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static long tick;
    private static double rotation;
    private static Random random = new Random();

    private static ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }

    public static void doRender(Entity entity, double x, double y, double z) {
        rotation = (double) (System.nanoTime() - tick) / 2500000 * ItemDummyContainer.rotateSpeed;
        if (!mc.inGameHasFocus) rotation = 0;
        EntityItem item = ((EntityItem) entity);

        ItemStack itemstack = item.getEntityItem();
        int i = itemstack != null && itemstack.getItem() != null ? Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata() : 187;
        random.setSeed(i);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getEntityTexture());
        Minecraft.getMinecraft().getTextureManager().getTexture(getEntityTexture())
            .setBlurMipmap(false, false);

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(itemstack);
        boolean flag1 = ibakedmodel.isGui3d();
        boolean is3D = ibakedmodel.isGui3d();
        int j = getModelCount(itemstack);

        GlStateManager.translate((float) x, (float) y, (float) z);

        if (ibakedmodel.isGui3d()) GlStateManager.scale(0.5F, 0.5F, 0.5F);

        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(item.rotationYaw, 0.0F, 0.0F, 1.0F);

        GlStateManager.translate(0, 0, is3D ? -0.08 : -0.04);

        //Handle Rotations
        if (is3D || mc.getRenderManager().options != null) {
            if (is3D) {
                if (!item.onGround) {
                    double rotation = ClientPhysic.rotation * 2;
                    item.rotationPitch += rotation;
                }
            } else {
                if (!Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(item.posZ) && item.worldObj != null) {
                    if (item.onGround) {
                        item.rotationPitch = 0;
                    } else {
                        double rotation = ClientPhysic.rotation * 2;
                        item.rotationPitch += rotation;
                    }
                }
            }

            GlStateManager.rotate(item.rotationPitch, 1, 0, 0.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int k = 0; k < j; k++) {
            GlStateManager.pushMatrix();
            if (flag1) {
                if (k > 0) {
                    float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f7, f9, f6);
                }

                mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            } else {
                mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.05375F);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(getEntityTexture());
        Minecraft.getMinecraft().getTextureManager().getTexture(getEntityTexture()).restoreLastBlurMipmap();
    }

    private static int getModelCount(ItemStack stack) {
        int i = 1;

        if (stack.stackSize > 48) i = 5;
        else if (stack.stackSize > 32) i = 4;
        else if (stack.stackSize > 16) i = 3;
        else if (stack.stackSize > 1) i = 2;
        return i;
    }
}
