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

package cc.hyperium.cosmetics.hats;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by ScottehBoeh - 10/13/2018
 */
public class CosmeticHat extends AbstractCosmetic {

    public ModelBase hatModel; /* Model for Hat */
    public ResourceLocation hatTexture; /* Texture for Hat */

    /**
     * Default Hat Cosmetic Constructor
     *
     * @param selfOnly     - Should be rendered on self only? (boolean)
     * @param purchaseType - Given Type of Purchase (EnumPurchaseType)
     */
    public CosmeticHat(boolean selfOnly, EnumPurchaseType purchaseType) {
        super(selfOnly, purchaseType);
    }

    /**
     * Set Model - Set the Hat Model and Texture
     *
     * @param givenModel   - Given Model (ModelBase)
     * @param givenTexture - Given Texture (ResourceLocation)
     * @return - Returns initial instance
     */
    public CosmeticHat setModel(ModelBase givenModel, ResourceLocation givenTexture) {
        hatModel = givenModel;
        hatTexture = givenTexture;
        return this;
    }

    @InvokeEvent
    public void onPlayerRender(RenderPlayerEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        AbstractClientPlayer player = e.getEntity();
        if (CosmeticsUtil.shouldHide(getPurchaseType())) return;

        if (isPurchasedBy(player.getUniqueID()) && !player.isInvisible()) {
            HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(player.getUniqueID());
            if (packageIfReady.getCachedSettings().getCurrentHatType() != getPurchaseType()) return;

            GlStateManager.pushMatrix();
            GlStateManager.translate(e.getX(), e.getY(), e.getZ());

            double scale = 1.0F;
            double rotate = interpolate(player.prevRotationYawHead, player.rotationYawHead, e.getPartialTicks());
            double rotate1 = interpolate(player.prevRotationPitch, player.rotationPitch, e.getPartialTicks());

            GL11.glScaled(-scale, -scale, scale);
            GL11.glTranslated(0.0, -((player.height - (player.isSneaking() ? .25 : 0)) - .38) / scale, 0.0);
            GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);
            GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);
            GlStateManager.translate(0, -.45, 0);

            /* Bind the hat texture and render the model */
            mc.getTextureManager().bindTexture(hatTexture);
            hatModel.render(player, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            GlStateManager.popMatrix();
        }
    }
}
