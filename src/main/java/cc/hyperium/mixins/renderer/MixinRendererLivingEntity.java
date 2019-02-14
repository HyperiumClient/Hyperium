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

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.renderer.HyperiumRenderLivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;


@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {

    @Shadow
    private List<LayerRenderer<T>> layerRenderers;


    private HyperiumRenderLivingEntity<T> hyperiumRenderLivingEntity = new HyperiumRenderLivingEntity<>((RendererLivingEntity) (Object) this);

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * @author Sk1er
     * @reason Fix Levelhead not rendering on self
     */
    @Overwrite
    protected boolean canRenderName(T entity) {
        if (Settings.BETTERF1 && Minecraft.getMinecraft().gameSettings.hideGUI) {
            return false;
        }
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;

        if (entity instanceof EntityPlayer) {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();

            if (team != null) {
                Team.EnumVisible team$enumvisible = team.getNameTagVisibility();

                switch (team$enumvisible) {
                    case ALWAYS:
                        return true;
                    case NEVER:
                        return false;
                    case HIDE_FOR_OTHER_TEAMS:
                        return team1 == null || team.isSameTeam(team1);
                    case HIDE_FOR_OWN_TEAM:
                        return team1 == null || !team.isSameTeam(team1);
                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }

    /**
     * @author Sk1er
     * @reason 1.7 Red Armor
     */
    @Overwrite
    protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        hyperiumRenderLivingEntity.renderLayers(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_, layerRenderers);
    }

    /**
     * @author Sk1er
     * @reason Flip Cosmetic
     */
    @Overwrite
    protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        hyperiumRenderLivingEntity.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
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


}
