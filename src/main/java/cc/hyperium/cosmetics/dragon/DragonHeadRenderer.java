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

package cc.hyperium.cosmetics.dragon;

import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

public class DragonHeadRenderer extends ModelBase {
    private Minecraft mc;
    private ModelRenderer jaw;
    private ModelRenderer head;
    private DragonCosmetic dragonCosmetic;
    private ResourceLocation selectedLoc;

    public DragonHeadRenderer(DragonCosmetic cosmetic) {
        dragonCosmetic = cosmetic;
        selectedLoc = new ResourceLocation("textures/entity/enderdragon/dragon.png");
        mc = Minecraft.getMinecraft();
        float f = -16.0F;
        textureWidth = 256;
        textureHeight = 256;
        setTextureOffset("body.body", 0, 0);
        setTextureOffset("wing.skin", -56, 88);
        setTextureOffset("wingtip.skin", -56, 144);
        setTextureOffset("rearleg.main", 0, 0);
        setTextureOffset("rearfoot.main", 112, 0);
        setTextureOffset("rearlegtip.main", 196, 0);
        setTextureOffset("head.upperhead", 112, 30);
        setTextureOffset("wing.bone", 112, 88);
        setTextureOffset("head.upperlip", 176, 44);
        setTextureOffset("jaw.jaw", 176, 65);
        setTextureOffset("frontleg.main", 112, 104);
        setTextureOffset("wingtip.bone", 112, 136);
        setTextureOffset("frontfoot.main", 144, 104);
        setTextureOffset("neck.box", 192, 104);
        setTextureOffset("frontlegtip.main", 226, 138);
        setTextureOffset("body.scale", 220, 53);
        setTextureOffset("head.scale", 0, 0);
        setTextureOffset("neck.scale", 48, 0);
        setTextureOffset("head.nostril", 112, 0);
        head = new ModelRenderer(this, "head");
        head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f, 12, 5, 16);
        head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f, 16, 16, 16);
        head.mirror = true;
        head.addBox("scale", -5.0F, -12.0F, 12.0F + f, 2, 4, 6);
        head.addBox("nostril", -5.0F, -3.0F, -6.0F + f, 2, 2, 4);
        head.mirror = false;
        head.addBox("scale", 3.0F, -12.0F, 12.0F + f, 2, 4, 6);
        head.addBox("nostril", 3.0F, -3.0F, -6.0F + f, 2, 2, 4);
        jaw = new ModelRenderer(this, "jaw");
        jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f);
        jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
        head.addChild(jaw);
    }

    @InvokeEvent
    public void onRenderPlayer(RenderPlayerEvent event) {
        if (CosmeticsUtil.shouldHide(EnumPurchaseType.DRAGON_HEAD)) return;
        EntityPlayer entity = event.getEntity();
        if (dragonCosmetic.isPurchasedBy(entity.getUniqueID()) && !entity.isInvisible()) {
            HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
            if (packageIfReady == null || packageIfReady.getCachedSettings().isDragonHeadDisabled()) return;

            GlStateManager.pushMatrix();
            GlStateManager.translate(event.getX(), event.getY(), event.getZ());
            renderHead(event.getEntity(), event.getPartialTicks());
            GlStateManager.popMatrix();
        }

    }

    private void renderHead(EntityPlayer player, float partialTicks) {
        double scale = 1.0F;
        double rotate = CosmeticsUtil.interpolate(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
        double rotate1 = CosmeticsUtil.interpolate(player.prevRotationPitch, player.rotationPitch, partialTicks);

        GlStateManager.scale(-scale, -scale, scale);
        GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);

        GlStateManager.translate(0.0, -(player.height - .4) / scale, 0.0);
        GlStateManager.translate(0.0D, 0.0D, .05 / scale);
        GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);
        GlStateManager.translate(0.0, -0.3 / scale, .06);

        if (player.isSneaking()) GlStateManager.translate(0.0, 0.125 / scale, 0.0);

        float[] colors = new float[]{1.0f, 1.0f, 1.0f};

        if (player.onGround) {
            jaw.rotateAngleX = 0;
        } else {
            if (mc.theWorld != null) {
                int e = -1;
                WorldClient theWorld = Minecraft.getMinecraft().theWorld;
                Chunk chunk = theWorld.getChunkFromBlockCoords(new BlockPos(player.posX, player.posY, player.posZ));

                for (int i = 0; i < 255; i++) {
                    if (i > player.posY)
                        break;
                    Block block = chunk.getBlock(new BlockPos(player.posX, i, player.posZ));
                    if (block != null && !block.getMaterial().equals(Material.air))
                        e = i;
                }

                jaw.rotateAngleX = 0;
                if (e != -1) {
                    double dis = Math.abs(e - player.posY);
                    dis /= 4;
                    if (dis != 0) {
                        jaw.rotateAngleX = (float) ((dis) / (1 + dis));
                    }
                }
            } else {
                jaw.rotateAngleX = 0;
            }
        }

        GlStateManager.color(colors[0], colors[1], colors[2]);
        mc.getTextureManager().bindTexture(selectedLoc);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        head.render(.1F);

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
}
