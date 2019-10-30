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

package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.mixins.client.renderer.entity.MixinRenderPlayer;
import cc.hyperium.mixinsimp.client.model.IMixinModelBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

/**
 * Used to render the armor right in two parts. Is called in
 * {@link MixinRenderPlayer#injectTwoPartLayerBipedArmor(LayerRenderer)} to
 * replace the normal {@link LayerBipedArmor}. Mixin would be preferred, but is
 * not possible as far as I know, since you don't know if
 * {@link LayerBipedArmor} is used for player armor rendering or other entity
 * armor rendering
 *
 * @author 9Y0
 */
public class TwoPartLayerBipedArmor extends LayerArmorBase<ModelBiped> {

    public TwoPartLayerBipedArmor(RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        modelLeggings = new ModelBiped(0.5F);
        modelArmor = new ModelBiped(1.0F);
    }

    /**
     * The only method which is edited. Shows some more model te make everything
     * render properly.
     *
     * @param model     The model which gets rendered
     * @param armorSlot The slot of the armor (1 = boots, 2 = leggins, etc..). Don't ask
     *                  me why it doesn't start at 0.
     * @author 9Y0
     */
    protected void setModelPartVisible(ModelBiped model, int armorSlot) {
        model.setInvisible(false);
        IMixinModelBiped modelBiped = (IMixinModelBiped) model;

        switch (armorSlot) {
            case 1: {
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                modelBiped.getBipedRightLowerLeg().showModel = true;
                modelBiped.getBipedLeftLowerLeg().showModel = true;
                break;
            }
            case 2: {
                model.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                modelBiped.getBipedRightLowerLeg().showModel = true;
                modelBiped.getBipedLeftLowerLeg().showModel = true;
                break;
            }
            case 3: {
                model.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                modelBiped.getBipedRightForeArm().showModel = true;
                modelBiped.getBipedLeftForeArm().showModel = true;
                break;
            }
            case 4: {
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                break;
            }
        }
    }
}
