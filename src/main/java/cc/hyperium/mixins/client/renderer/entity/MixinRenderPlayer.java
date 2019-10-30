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

package cc.hyperium.mixins.client.renderer.entity;

import cc.hyperium.event.render.RenderNameTagEvent;
import cc.hyperium.mixinsimp.client.renderer.entity.TwoPartLayerBipedArmor;
import cc.hyperium.mixinsimp.client.renderer.entity.HyperiumRenderPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity<AbstractClientPlayer> {

    private HyperiumRenderPlayer hyperiumRenderPlayer = new HyperiumRenderPlayer((RenderPlayer) (Object) this);

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
    public <V extends EntityLivingBase, U extends LayerRenderer<V>> U injectTwoPartLayerBipedArmor(U original) {
        return (U) new TwoPartLayerBipedArmor(this);
    }

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GlStateManager.resetColor();

        hyperiumRenderPlayer.doRender(entity, x, y, z, partialTicks, renderManager);
    }

    /**
     * Fixes bug MC-1349
     *
     * @param clientPlayer - User
     * @param ci           - Callback
     */
    @Inject(method = "renderRightArm", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;isSneak:Z", ordinal = 0))
    private void onUpdateTimer(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        hyperiumRenderPlayer.onUpdateTimer();
    }

    /**
     * @author Sk1er
     * @reason Cancel nametag render event when score is renderer
     */
    @Overwrite
    protected void renderOffsetLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        if (p_177069_10_ < 100.0D) {
            Scoreboard scoreboard = entityIn.getWorldScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);

            if (scoreobjective != null) {
                Score score = scoreboard.getValueFromObjective(entityIn.getName(), scoreobjective);
                RenderNameTagEvent.CANCEL = true;
                if (entityIn != Minecraft.getMinecraft().thePlayer) {
                    renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                    y += (float) getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * p_177069_9_;
                }

                RenderNameTagEvent.CANCEL = false;
            }
        }

        super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
    }
}

