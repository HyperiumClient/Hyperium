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

package cc.hyperium.cosmetics.wings;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WingsRenderer extends ModelBase {

    private Minecraft mc;
    private ModelRenderer wing;
    private ModelRenderer wingTip;
    private WingsCosmetic wingsCosmetic;

    public WingsRenderer(WingsCosmetic cosmetic) {
        wingsCosmetic = cosmetic;
        mc = Minecraft.getMinecraft();
        setTextureOffset("wing.bone", 0, 0);
        setTextureOffset("wing.skin", -10, 8);
        setTextureOffset("wingtip.bone", 0, 5);
        setTextureOffset("wingtip.skin", -10, 18);
        (wing = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
        wing.setRotationPoint(-2.0F, 0.0F, 0.0F);
        wing.addBox("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2);
        wing.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
        (wingTip = new ModelRenderer(this, "wingtip")).setTextureSize(30, 30);
        wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F);
        wingTip.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1);
        wingTip.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
        wing.addChild(wingTip);
    }

    @InvokeEvent
    public void onRenderPlayer(RenderPlayerEvent event) {
        if (CosmeticsUtil.shouldHide(EnumPurchaseType.WING_COSMETIC)) return;
        EntityPlayer player = event.getEntity();
        if (wingsCosmetic.isPurchasedBy(event.getEntity().getUniqueID()) && !player.isInvisible()) {
            HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
            if (packageIfReady == null || packageIfReady.getCachedSettings().isWingsDisabled()) return;
            renderWings(player, event.getPartialTicks(), event.getX(), event.getY(), event.getZ());
        }
    }

    private void renderWings(EntityPlayer player, float partialTicks, double x, double y, double z) {
        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(player.getUniqueID());
        if (packageIfReady == null) return;

        String s = packageIfReady.getCachedSettings().getWingsType();
        ResourceLocation location = wingsCosmetic.getLocation(s);

        // Wings scale as defined in the settings.
        double v = packageIfReady.getCachedSettings().getWingsScale();
        double scale = v / 100.0;
        double rotate = CosmeticsUtil.interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);

        GlStateManager.pushMatrix();
        // Displaces the wings by a custom value.
        double customOffset = Settings.WINGS_OFFSET / 50;
        GlStateManager.translate(0, customOffset, 0);
        GlStateManager.translate(x, y, z);


        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.rotate((float) (180.0F + rotate), 0.0F, 1.0F, 0.0F);

        // Height of the player.
        float scaledPlayerHeight = (float) (1.85F / scale);

        // Height of the wings from the feet.
        float scaledHeight = (float) (1.25 / scale);

        // Moves the wings to the top of the player's head then backward slightly (away from the centre).
        GlStateManager.translate(0.0F, -scaledHeight, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.15F / scale);

        // Takes into account player flip cosmetic.
        int rotateState = Hyperium.INSTANCE.getHandlers().getFlipHandler().get(player.getUniqueID());

        if (player.isSneaking() && rotateState == 0) {
            GlStateManager.translate(0.0F, 0.125 / scale, 0.0F);
        } else if (player.isSneaking() && rotateState == 1) {
            GlStateManager.translate(0.0F, -0.125 / scale, 0.0F);
        }

        // Spinning rotate mode.
        if (rotateState == 2) {
            // Translate to centre of the player.
            float difference = scaledHeight - (scaledPlayerHeight / 2);
            GlStateManager.translate(0.0F, difference, 0.0F);

            // Rotate.
            double l = System.currentTimeMillis() % (360 * 1.75) / 1.75;
            GlStateManager.rotate((float) -l, 0.1F, 0.0F, 0.0F);

            //Translate back up to the correct position.
            GlStateManager.translate(0.0F, -difference, 0.0F);
        } else if (rotateState == 1) {
            // Flip rotate mode.
            float difference = scaledPlayerHeight - scaledHeight;

            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, -scaledHeight + difference, 0.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F);

        mc.getTextureManager().bindTexture(location);

        for (int j = 0; j < 2; j++) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            float f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0F;
            wing.rotateAngleX = (float) Math.toRadians(-80.0) - (float) Math.cos(f11) * 0.2F;
            wing.rotateAngleY = (float) Math.toRadians(20.0) + (float) Math.sin(f11) * 0.4F;
            wing.rotateAngleZ = (float) Math.toRadians(20.0);
            wingTip.rotateAngleZ = -(float) (Math.sin(f11 + 2.0F) + 0.5) * 0.75F;
            wing.render(0.0625F);
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
            if (j == 0) GL11.glCullFace(GL11.GL_FRONT);
        }

        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
