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

import cc.hyperium.mixinsimp.renderer.HyperiumRenderLivingEntity;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
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


    @Shadow
    protected abstract float getDeathMaxRotation(T entityLivingBaseIn);

    @Shadow
    protected abstract boolean canRenderName(T entity);

    /**
     * @author
     */
    @Overwrite
    protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        hyperiumRenderLivingEntity.renderLayers(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_, layerRenderers);
    }

    /**
     * @author Hyperium
     */
    @Overwrite
    protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        hyperiumRenderLivingEntity.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
    }

    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }

    @Overwrite
    public void renderName(T entity, double x, double y, double z) {
        hyperiumRenderLivingEntity.renderName(entity, x, y, z,renderManager);
    }
}
