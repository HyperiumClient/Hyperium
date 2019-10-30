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

package cc.hyperium.mixins.client.model;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.model.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.event.model.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.client.model.IMixinModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class MixinModelPlayer extends MixinModelBiped implements IMixinModelPlayer {

    /** Will now be the left upperarm wear. */
    @Shadow public ModelRenderer bipedLeftArmwear;
    /** Will now be the right upperarm wear.  */
    @Shadow public ModelRenderer bipedRightArmwear;
    /** Will now be the left upperleg wear. */
    @Shadow public ModelRenderer bipedLeftLegwear;
    /** Will now be the right upperleg wear. */
    @Shadow public ModelRenderer bipedRightLegwear;
    @Shadow public ModelRenderer bipedBodyWear;
    @Shadow private ModelRenderer bipedCape;

    private ModelRenderer bipedLeftForeArmwear;
    private ModelRenderer bipedRightForeArmwear;
    private ModelRenderer bipedLeftLowerLegwear;
    private ModelRenderer bipedRightLowerLegwear;

    private ModelRenderer butt;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectModelChanges(float modelSize, boolean useSmallArms, CallbackInfo ci) {
        if (useSmallArms) {
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, modelSize);
            bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            bipedLeftForeArm = new ModelRenderer(this, 32, 54);
            bipedLeftForeArm.addBox(-1.0F, 4.0F, -2.0F, 3, 6, 4, modelSize);
            bipedLeftForeArm.setRotationPoint(5.0F, 2.5F, 0.0F);


            bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 6, 4, modelSize + 0.25F);
            bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
            bipedLeftForeArmwear = new ModelRenderer(this, 48, 54);
            bipedLeftForeArmwear.addBox(-1.0F, 4.0F, -2.0F, 3, 6, 4, modelSize + 0.25F);
            bipedLeftForeArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

            bipedRightArm = new ModelRenderer(this, 40, 16);
            bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, modelSize);
            bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            bipedRightForeArm = new ModelRenderer(this, 40, 22);
            bipedRightForeArm.addBox(-2.0F, 4.0F, -2.0F, 3, 6, 4, modelSize);
            bipedRightForeArm.setRotationPoint(-5.0F, 2.5F, 0.0F);


            bipedRightArmwear = new ModelRenderer(this, 40, 32);
            bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 6, 4, modelSize + 0.25F);
            bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);

            bipedRightForeArmwear = new ModelRenderer(this, 40, 38);
            bipedRightForeArmwear.addBox(-2.0F, 4.0F, -2.0F, 3, 6, 4, modelSize + 0.25F);
            bipedRightForeArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);


        } else {
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            bipedLeftForeArm = new ModelRenderer(this, 32, 54);
            bipedLeftForeArm.addBox(-1.0F, 4.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftForeArm.setRotationPoint(5.0F, 2.0F, 0.0F);


            bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
            bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
            bipedLeftForeArmwear = new ModelRenderer(this, 48, 54);
            bipedLeftForeArmwear.addBox(-1.0F, 4.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
            bipedLeftForeArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

            bipedRightArm = new ModelRenderer(this, 40, 16);
            bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
            bipedRightForeArm = new ModelRenderer(this, 40, 22);
            bipedRightForeArm.addBox(-3.0F, 4.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightForeArm.setRotationPoint(-5.0F, 2.0F, 0.0F);


            bipedRightArmwear = new ModelRenderer(this, 40, 32);
            bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
            bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

            bipedRightForeArmwear = new ModelRenderer(this, 40, 38);
            bipedRightForeArmwear.addBox(-3.0F, 4.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
            bipedRightForeArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }

        butt = new ModelRenderer(this, 16, 16 + 8);
        butt.addBox(-4.0F, 0.0F, 0.0F, 8, 4, 4, modelSize);
        butt.setRotationPoint(0, 16, 0);
        butt.showModel = false;

        bipedLeftLeg = new ModelRenderer(this, 16, 48);
        bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        bipedLeftLowerLeg = new ModelRenderer(this, 16, 54);
        bipedLeftLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
        bipedLeftLowerLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

        bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
        bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        bipedLeftLowerLegwear = new ModelRenderer(this, 0, 54);
        bipedLeftLowerLegwear.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
        bipedLeftLowerLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bipedRightLowerLeg = new ModelRenderer(this, 0, 22);
        bipedRightLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
        bipedRightLowerLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

        bipedRightLegwear = new ModelRenderer(this, 0, 32);
        bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
        bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        bipedRightLowerLegwear = new ModelRenderer(this, 0, 38);
        bipedRightLowerLegwear.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize + 0.25F);
        bipedRightLowerLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

        butt.showModel = false;
        fixTopAndBottomOfLimbWrongTextures(
                bipedLeftForeArm, bipedLeftForeArmwear, //
                bipedRightForeArm, bipedRightForeArmwear, //
                bipedLeftLowerLeg, bipedLeftLowerLegwear, //
                bipedRightLowerLeg, bipedRightLowerLegwear //
        );
    }

    /**
     * @author 9Y0, Mojang
     * @reason body parts
     */
    @Overwrite
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();

        if (isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
        } else {
            if (entityIn.isSneaking()) GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }

        bipedLeftLegwear.render(scale);
        bipedRightLegwear.render(scale);
        bipedLeftArmwear.render(scale);
        bipedRightArmwear.render(scale);
        bipedBodyWear.render(scale);
        bipedLeftForeArmwear.render(scale);
        bipedLeftForeArm.render(scale);
        bipedRightForeArmwear.render(scale);
        bipedRightForeArm.render(scale);
        bipedLeftLowerLeg.render(scale);
        bipedLeftLowerLegwear.render(scale);
        bipedRightLowerLeg.render(scale);
        bipedRightLowerLegwear.render(scale);
        butt.render(scale);

        GlStateManager.popMatrix();
    }

    @Inject(method = "renderRightArm", at = @At("RETURN"))
    private void renderRightArm(CallbackInfo ci) {
        bipedRightForeArm.render(0.0625F);
        bipedRightForeArmwear.render(0.0625F);
    }

    @Inject(method = "renderLeftArm", at = @At("RETURN"))
    private void renderLeftArm(CallbackInfo ci) {
        bipedLeftForeArm.render(0.0625F);
        bipedLeftForeArmwear.render(0.0625F);
    }

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    private void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn, CallbackInfo ci) {
        // This should always be the case I guess, but we better be safe
        if (entityIn instanceof AbstractClientPlayer) {
            EventBus.INSTANCE.post(new PreCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
        }

        copyModelAnglesAndOffest(bipedLeftArm, bipedLeftForeArm);
        copyModelAnglesAndOffest(bipedRightArm, bipedRightForeArm);
        copyModelAnglesAndOffest(bipedLeftArmwear, bipedLeftForeArmwear);
        copyModelAnglesAndOffest(bipedRightArmwear, bipedRightForeArmwear);

        copyModelAnglesAndOffest(bipedLeftLeg, bipedLeftLowerLeg);
        copyModelAnglesAndOffest(bipedRightLeg, bipedRightLowerLeg);
        copyModelAnglesAndOffest(bipedLeftLegwear, bipedLeftLowerLegwear);
        copyModelAnglesAndOffest(bipedRightLegwear, bipedRightLowerLegwear);

        if (entityIn instanceof AbstractClientPlayer) {
            EventBus.INSTANCE.post(new PostCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
        }
    }

    @Inject(method = "setInvisible", at = @At("RETURN"))
    private void setInvisible(boolean invisble, CallbackInfo ci) {
        bipedLeftForeArmwear.showModel = invisble;
        bipedRightForeArmwear.showModel = invisble;
        bipedLeftLowerLegwear.showModel = invisble;
        bipedRightLowerLegwear.showModel = invisble;
        butt.showModel = invisble;

    }

    @Override
    public ModelRenderer getButt() {
        return butt;
    }

    /* Right leg wrappers */
    @Override
    public ModelRenderer getBipedRightUpperLegwear() {
        return bipedRightLegwear;
    }

    @Override
    public ModelRenderer getBipedRightLowerLegwear() {
        return bipedRightLowerLegwear;
    }

    /* Left leg wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperLegwear() {
        return bipedLeftLegwear;
    }

    @Override
    public ModelRenderer getBipedLeftLowerLegwear() {
        return bipedLeftLowerLegwear;
    }

    /* Right arm wrappers */
    @Override
    public ModelRenderer getBipedRightUpperArmwear() {
        return bipedRightArmwear;
    }

    @Override
    public ModelRenderer getBipedRightForeArmwear() {
        return bipedRightForeArmwear;
    }

    /* Left arm wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperArmwear() {
        return bipedLeftArmwear;
    }

    @Override
    public ModelRenderer getBipedLeftForeArmwear() {
        return bipedLeftForeArmwear;
    }

    /* Body wrappers */
    @Override
    public ModelRenderer getBipedBodywear() {
        return bipedBodyWear;
    }

    @Override
    public ModelRenderer getCape() {
        return bipedCape;
    }


}
