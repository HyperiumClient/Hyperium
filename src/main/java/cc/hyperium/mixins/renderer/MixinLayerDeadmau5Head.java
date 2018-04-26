/*
 *      Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published
 *      by the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.EarsCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerDeadmau5Head.class)
public class MixinLayerDeadmau5Head {

    @Shadow
    private final RenderPlayer playerRenderer;

    public MixinLayerDeadmau5Head(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    /**
     * @author
     */
    @Inject(method = "doRenderLayer", at = @At("HEAD"))
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_,
                              float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        if (entitylivingbaseIn.isInvisible())
            return;
        if (entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
            return;

        if (!Hyperium.INSTANCE.getHandlers().getConfigOptions().showCosmeticsEveryWhere) {
            if (!(Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName().equalsIgnoreCase("HOUSING") || Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("lobby")))
                return;
        }
        if (Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isPurchasedBy(entitylivingbaseIn.getUniqueID()) && !(entitylivingbaseIn.getName().equals("deadmau5"))) {
            HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(entitylivingbaseIn.getUniqueID());
            if (packageIfReady == null) {

                return;
            }
            AbstractHyperiumPurchase purchase = packageIfReady.getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC);
            if (purchase == null) {

                return;
            }
            if (entitylivingbaseIn.getUniqueID() != Minecraft.getMinecraft().thePlayer.getUniqueID()) {
                if (!((EarsCosmetic) purchase).isEnabled()) {
                    return;
                }
            } else if (!Hyperium.INSTANCE.getHandlers().getConfigOptions().enableDeadmau5Ears)
                return;
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

            for (int i = 0; i < 2; ++i) {
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                GlStateManager.pushMatrix();
                GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0.375F * (float) (i * 2 - 1), 0.0F, 0.0F);
                GlStateManager.translate(0.0F, -0.375F, 0.0F);
                GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
                if (entitylivingbaseIn.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.25, 0.0F);
                }
                float f2 = 1.3333334F;
                GlStateManager.scale(f2, f2, f2);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                GlStateManager.popMatrix();
            }
        }
    }
}
