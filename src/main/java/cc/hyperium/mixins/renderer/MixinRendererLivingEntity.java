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
import cc.hyperium.gui.settings.items.AnimationSettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;


@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T> {
    @Shadow
    protected List<LayerRenderer<T>> layerRenderers;

    protected MixinRendererLivingEntity(RenderManager renderManager) {
        super(renderManager);
    }

    @Shadow
    protected abstract void unsetBrightness();

    @Shadow
    protected abstract boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures);

    @Shadow
    protected abstract float getDeathMaxRotation(T entityLivingBaseIn);

    /**
     * @author
     */
    @Overwrite
    protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
            boolean f = layerrenderer.shouldCombineTextures();
            if (AnimationSettings.redArmour && layerrenderer instanceof LayerBipedArmor) {
                f = true;
            }
            boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks, f);
            layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);

            if (flag) {
                this.unsetBrightness();
            }
        }
    }

    @Overwrite
    protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (bat.deathTime > 0) {
            float f = ((float) bat.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * this.getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
        } else {
            Hyperium.INSTANCE.getHandlers().getFlipHandler().transform(bat);
        }
    }

    @Override
    public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ);
    }
}
