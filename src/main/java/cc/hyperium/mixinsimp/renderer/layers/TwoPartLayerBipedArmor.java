package cc.hyperium.mixinsimp.renderer.layers;

import cc.hyperium.mixins.renderer.MixinRenderPlayer;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
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
        this.field_177189_c = new ModelBiped(0.5F);
        this.field_177186_d = new ModelBiped(1.0F);
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
    protected void func_177179_a(ModelBiped model, int armorSlot) {
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
