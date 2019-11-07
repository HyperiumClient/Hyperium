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

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.EntityRenderEvent;
import cc.hyperium.mixinsimp.client.renderer.entity.HyperiumRendererLivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    @Shadow protected List<LayerRenderer<T>> layerRenderers;

    private HyperiumRendererLivingEntity<T> hyperiumRenderLivingEntity = new HyperiumRendererLivingEntity<T>((RendererLivingEntity<T>) (Object) this);

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author Sk1er
     * @reason Fix Levelhead not rendering on self
     */
    @Overwrite
    protected boolean canRenderName(T entity) {
        if (Settings.BETTERF1 && Minecraft.getMinecraft().gameSettings.hideGUI) return false;
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;

        if (entity instanceof EntityPlayer) {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();

            if (team != null) {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();

                switch (team$enumvisible) {
                    case NEVER:
                        return false;
                    case HIDE_FOR_OTHER_TEAMS:
                        return team1 == null || team.isSameTeam(team1);
                    case HIDE_FOR_OWN_TEAM:
                        return team1 == null || !team.isSameTeam(team1);
                    case ALWAYS:
                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }

    /**
     * @author Sk1er
     * @reason 1.7 Red Armor
     */
    @Overwrite
    protected void renderLayers(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch, float scale) {
        hyperiumRenderLivingEntity.renderLayers(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, layerRenderers);
    }

    /**
     * @author Sk1er
     * @reason Flip Cosmetic
     */
    @Overwrite
    protected void rotateCorpse(T bat, float p_77043_2_, float rotation, float partialTicks) {
        hyperiumRenderLivingEntity.rotateCorpse(bat, rotation, partialTicks);
    }

    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }

    /**
     * @author sk1er
     * @reason we do it better
     */
    @Overwrite
    public void renderName(T entity, double x, double y, double z) {
        hyperiumRenderLivingEntity.renderName(entity, x, y, z, renderManager);
    }

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Settings.DISABLE_ARMORSTANDS && entity instanceof EntityArmorStand) ci.cancel();

        final EntityRenderEvent event = new EntityRenderEvent(entity, (float) x, (float) y, (float) z, entity.rotationPitch, entityYaw, 1.0F);
        EventBus.INSTANCE.post(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
