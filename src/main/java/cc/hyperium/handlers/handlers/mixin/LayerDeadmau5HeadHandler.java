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

package cc.hyperium.handlers.handlers.mixin;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.EarsCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class LayerDeadmau5HeadHandler {

  public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float partialTicks,
      RenderPlayer playerRenderer) {
    try {
      if (entitylivingbaseIn == null || entitylivingbaseIn.isInvisible() || entitylivingbaseIn
          .isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
        return;
      }

      if (!Settings.SHOW_COSMETICS_EVERYWHERE && !(Hyperium.INSTANCE.getHandlers()
          .getLocationHandler().isLobbyOrHousing())) {
        return;
      }

      String name = entitylivingbaseIn.getName();

      if (name == null) {
        return;
      }

      if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic()
          .isPurchasedBy(entitylivingbaseIn.getUniqueID())) {
        HyperiumPurchase packageIfReady = PurchaseApi.getInstance()
            .getPackageIfReady(entitylivingbaseIn.getUniqueID());

        if (packageIfReady == null) {
          return;
        }

        AbstractHyperiumPurchase purchase = packageIfReady
            .getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC);

        if (purchase == null) {
          return;
        }

        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;

        if (thePlayer != null && entitylivingbaseIn.getUniqueID() != thePlayer.getUniqueID()) {
          if (!((EarsCosmetic) purchase).isEnabled()) {
            return;
          }
        } else if (!Settings.EARS_STATE.equalsIgnoreCase("yes")) {
          return;
        }

        ResourceLocation locationSkin = entitylivingbaseIn.getLocationSkin();

        if (locationSkin != null) {
          playerRenderer.bindTexture(locationSkin);
        }

        GlStateManager.disableCull();

        for (int i = 0; i < 2; ++i) {

          try {
            GlStateManager.pushMatrix();
            float f = entitylivingbaseIn.prevRotationYaw
                + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) *
                partialTicks - (entitylivingbaseIn.prevRenderYawOffset
                + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset)
                * partialTicks);
            float f1 = entitylivingbaseIn.prevRotationPitch
                + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch)
                * partialTicks;
            GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.375F * (float) ((i << 1) - 1), 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.375F, 0.0F);
            GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);

            if (entitylivingbaseIn.isSneaking()) {
              GlStateManager.translate(0.0F, 0.25, 0.0F);
            }

            float f2 = 1.3333334F;
            GlStateManager.scale(f2, f2, f2);
            playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            GlStateManager.popMatrix();

          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
