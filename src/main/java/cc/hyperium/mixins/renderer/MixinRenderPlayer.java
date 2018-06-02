/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.mixinsimp.renderer.layers.TwoPartLayerBipedArmor;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {

    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    /**
     * Not using the normal armor layer, but a slightly modified one. This is done
     * to prevent weird rendering bugs because of armor being on different layers in
     * the textures. These bugs were caused because the armor needs to be slit up in
     * two for the knees and elbows
     *
     * @author 9Y0
     */
    @SuppressWarnings("unchecked")
    @ModifyArg(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;addLayer(Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;)Z"))
    private <V extends EntityLivingBase, U extends LayerRenderer<V>> U injectTwoPartLayerBipedArmor(U original) {
        return (U) new TwoPartLayerBipedArmor(this);
    }

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().turnPeopleIntoBlock) {
            try {
                ci.cancel();
                Hyperium.INSTANCE.getHandlers().getRenderPlayerAsBlock().reDraw(entity,x,y,z);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            EventBus.INSTANCE.post(new RenderPlayerEvent(entity, renderManager, x, y, z, partialTicks));

    }

    /**
     * Fixes bug MC-1349
     *
     * @param clientPlayer
     * @param ci
     */
    @Inject(method = "renderRightArm", at = @At(value = "FIELD", ordinal = 3))
    private void onUpdateTimer(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        ModelPlayer modelplayer = this.getMainModel();
        modelplayer.isRiding = modelplayer.isSneak = false;
    }
}

