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

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.model.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.event.model.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.client.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.client.model.IMixinModelBox;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ModelBiped.class)
public class MixinModelBiped extends ModelBase implements IMixinModelBiped {

    @Shadow public ModelRenderer bipedHead;
    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedBody;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public int heldItemLeft;
    @Shadow public int heldItemRight;
    @Shadow public boolean isSneak;
    @Shadow public boolean aimedBow;

    ModelRenderer bipedLeftForeArm;
    ModelRenderer bipedRightForeArm;
    ModelRenderer bipedLeftLowerLeg;
    ModelRenderer bipedRightLowerLeg;

    @Inject(method = "<init>(FFII)V", at = @At("RETURN"))
    private void injectModelChanges(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn, CallbackInfo ci) {
        // Doing these checks because other ModelBiped (ModelZombie, ModelArmorStand etc) were messed up
        // The only ModelBiped which isn't extended is armor as far as I know. If not the check should be more specific for armor.
        if (getClass().equals(ModelBiped.class)) {
            bipedRightArm = new ModelRenderer(this, 40, 16);
            bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
            bipedRightForeArm = new ModelRenderer(this, 40, 22);
            bipedRightForeArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightForeArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
            bipedLeftArm = new ModelRenderer(this, 40, 16);
            bipedLeftArm.mirror = true;
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
            bipedLeftForeArm = new ModelRenderer(this, 40, 22);
            bipedLeftForeArm.mirror = true;
            bipedLeftForeArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftForeArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);

            bipedRightLeg = new ModelRenderer(this, 0, 16);
            bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
            bipedRightLowerLeg = new ModelRenderer(this, 0, 22);
            bipedRightLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
            bipedRightLowerLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);


            bipedLeftLeg = new ModelRenderer(this, 0, 16);
            bipedLeftLeg.mirror = true;
            bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
            bipedLeftLowerLeg = new ModelRenderer(this, 0, 22);
            bipedLeftLowerLeg.mirror = true;
            bipedLeftLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
            bipedLeftLowerLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);

            fixTopAndBottomOfLimbWrongTextures(bipedLeftForeArm, bipedRightForeArm, bipedLeftLowerLeg,
                    bipedRightLowerLeg);
        }
    }

    /**
     * Fixes boxes having the wrong textures, when they are the second part of a limb
     *
     * @param models The {@link ModelRenderer} parts you want to fix
     */
    void fixTopAndBottomOfLimbWrongTextures(ModelRenderer... models) {
        for (ModelRenderer model : models) {
            // We only need the first box since we know there only is one
            ModelBox box = model.cubeList.get(0);
            ((IMixinModelBox) box).offsetTextureQuad(model, 3, 0.0F, -6.0F);
        }
    }


    /**
     * @author 9Y0, Mojang
     * @reason body parts
     */
    @Overwrite
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();
        if (isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
        } else {
            if (entityIn.isSneaking()) GlStateManager.translate(0.0F, 0.2F, 0.0F);
            bipedHead.render(scale);
        }

        // Adding our parts
        bipedBody.render(scale);
        bipedRightArm.render(scale);
        bipedLeftArm.render(scale);
        bipedRightLeg.render(scale);
        bipedLeftLeg.render(scale);
        bipedHeadwear.render(scale);
        if (getClass().equals(ModelBiped.class)) renderBiped(scale);
        GlStateManager.popMatrix();
    }

    private void renderBiped(float scale) {
        bipedLeftForeArm.render(scale);
        bipedRightForeArm.render(scale);
        bipedLeftLowerLeg.render(scale);
        bipedRightLowerLeg.render(scale);
    }

    /**
     * @author Amplifiable
     * @reason 1.7 Blocking & Item Held
     */
    @Overwrite
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        bipedHead.rotateAngleY = netHeadYaw / 57.295776f;
        bipedHead.rotateAngleX = headPitch / 57.295776f;
        bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f;
        bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f;
        bipedRightArm.rotateAngleZ = 0.0f;
        bipedLeftArm.rotateAngleZ = 0.0f;
        bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
        bipedRightLeg.rotateAngleY = 0.0f;
        bipedLeftLeg.rotateAngleY = 0.0f;
        if (isRiding) {
            ModelRenderer bipedRightArm = this.bipedRightArm;
            bipedRightArm.rotateAngleX -= 0.62831855f;
            ModelRenderer bipedLeftArm = this.bipedLeftArm;
            bipedLeftArm.rotateAngleX -= 0.62831855f;
            bipedRightLeg.rotateAngleX = -1.2566371f;
            bipedLeftLeg.rotateAngleX = -1.2566371f;
            bipedRightLeg.rotateAngleY = 0.31415927f;
            bipedLeftLeg.rotateAngleY = -0.31415927f;
        }

        if (heldItemLeft != 0) {
            bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5f - 0.31415927f * heldItemLeft;
        }

        bipedRightArm.rotateAngleY = 0.0f;
        bipedRightArm.rotateAngleZ = 0.0f;
        switch (heldItemRight) {
            case 1:
                bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * heldItemRight;
                break;

            case 3:
                bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * heldItemRight;
                if (!Settings.OLD_BLOCKING) {
                    bipedRightArm.rotateAngleY = -0.5235988f;
                    break;
                }
                break;

        }

        bipedLeftArm.rotateAngleY = 0.0f;
        if (swingProgress > -9990.0f) {
            float f = swingProgress;
            bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f) * 3.1415927f * 2.0f) * 0.2f;
            bipedRightArm.rotationPointZ = MathHelper.sin(bipedBody.rotateAngleY) * 5.0f;
            bipedRightArm.rotationPointX = -MathHelper.cos(bipedBody.rotateAngleY) * 5.0f;
            bipedLeftArm.rotationPointZ = -MathHelper.sin(bipedBody.rotateAngleY) * 5.0f;
            bipedLeftArm.rotationPointX = MathHelper.cos(bipedBody.rotateAngleY) * 5.0f;
            ModelRenderer bipedRightArm2 = bipedRightArm;
            bipedRightArm2.rotateAngleY += bipedBody.rotateAngleY;
            ModelRenderer bipedLeftArm2 = bipedLeftArm;
            bipedLeftArm2.rotateAngleY += bipedBody.rotateAngleY;
            ModelRenderer bipedLeftArm3 = bipedLeftArm;
            bipedLeftArm3.rotateAngleX += bipedBody.rotateAngleY;
            f = 1.0f - swingProgress;
            f *= f;
            f *= f;
            f = 1.0f - f;

            float f2 = MathHelper.sin(f * 3.1415927f);
            float f3 = MathHelper.sin(swingProgress * 3.1415927f) * -(bipedHead.rotateAngleX - 0.7f) * 0.75f;
            bipedRightArm.rotateAngleX -= (float) (f2 * 1.2 + f3);
            ModelRenderer bipedRightArm3 = bipedRightArm;
            bipedRightArm3.rotateAngleY += bipedBody.rotateAngleY * 2.0f;
            ModelRenderer bipedRightArm4 = bipedRightArm;
            bipedRightArm4.rotateAngleZ += MathHelper.sin(swingProgress * 3.1415927f) * -0.4f;
        }

        if (isSneak) {
            bipedBody.rotateAngleX = 0.5f;
            ModelRenderer bipedRightArm5 = bipedRightArm;
            bipedRightArm5.rotateAngleX += 0.4f;
            ModelRenderer bipedLeftArm4 = bipedLeftArm;
            bipedLeftArm4.rotateAngleX += 0.4f;
            bipedRightLeg.rotationPointZ = 4.0f;
            bipedLeftLeg.rotationPointZ = 4.0f;
            bipedRightLeg.rotationPointY = 9.0f;
            bipedLeftLeg.rotationPointY = 9.0f;
            bipedHead.rotationPointY = 1.0f;
        } else {
            bipedBody.rotateAngleX = 0.0f;
            bipedRightLeg.rotationPointZ = 0.1f;
            bipedLeftLeg.rotationPointZ = 0.1f;
            bipedRightLeg.rotationPointY = 12.0f;
            bipedLeftLeg.rotationPointY = 12.0f;
            bipedHead.rotationPointY = 0.0f;
        }

        ModelRenderer bipedRightArm6 = bipedRightArm;
        bipedRightArm6.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        ModelRenderer bipedLeftArm5 = bipedLeftArm;
        bipedLeftArm5.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        ModelRenderer bipedRightArm7 = bipedRightArm;
        bipedRightArm7.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        ModelRenderer bipedLeftArm6 = bipedLeftArm;
        bipedLeftArm6.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;

        if (aimedBow) {
            float f4 = 0.0f;
            float f5 = 0.0f;
            bipedRightArm.rotateAngleZ = 0.0f;
            bipedLeftArm.rotateAngleZ = 0.0f;
            bipedRightArm.rotateAngleY = -(0.1f - f4 * 0.6f) + bipedHead.rotateAngleY;
            bipedLeftArm.rotateAngleY = 0.1f - f4 * 0.6f + bipedHead.rotateAngleY + 0.4f;
            bipedRightArm.rotateAngleX = -1.5707964f + bipedHead.rotateAngleX;
            bipedLeftArm.rotateAngleX = -1.5707964f + bipedHead.rotateAngleX;
            ModelRenderer bipedRightArm8 = bipedRightArm;
            bipedRightArm8.rotateAngleX -= f4 * 1.2f - f5 * 0.4f;
            ModelRenderer bipedLeftArm7 = bipedLeftArm;
            bipedLeftArm7.rotateAngleX -= f4 * 1.2f - f5 * 0.4f;
            ModelRenderer bipedRightArm9 = bipedRightArm;
            bipedRightArm9.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            ModelRenderer bipedLeftArm8 = bipedLeftArm;
            bipedLeftArm8.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            ModelRenderer bipedRightArm10 = bipedRightArm;
            bipedRightArm10.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            ModelRenderer bipedLeftArm9 = bipedLeftArm;
            bipedLeftArm9.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }

        copyModelAngles(bipedHead, bipedHeadwear);
        if (getClass().equals(ModelBiped.class)) {
            boolean isPlayer = entityIn instanceof AbstractClientPlayer;
            if (isPlayer) {
                EventBus.INSTANCE.post(new PreCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
            }

            copyModelAnglesAndOffest(bipedLeftArm, bipedLeftForeArm);
            copyModelAnglesAndOffest(bipedRightArm, bipedRightForeArm);

            copyModelAnglesAndOffest(bipedLeftLeg, bipedLeftLowerLeg);
            copyModelAnglesAndOffest(bipedRightLeg, bipedRightLowerLeg);

            if (isPlayer) {
                EventBus.INSTANCE.post(new PostCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
            }
        }
    }

    void copyModelAnglesAndOffest(ModelRenderer src, ModelRenderer dest) {
        copyModelAngles(src, dest);
        dest.offsetX = src.offsetX;
        dest.offsetY = src.offsetY;
        dest.offsetZ = src.offsetZ;
    }

    @Inject(method = "setInvisible", at = @At("RETURN"))
    private void setInvisible(boolean invisible, CallbackInfo ci) {
        if (getClass().equals(ModelBiped.class)) {
            bipedLeftForeArm.showModel = invisible;
            bipedRightForeArm.showModel = invisible;
            bipedLeftLowerLeg.showModel = invisible;
            bipedRightLowerLeg.showModel = invisible;
        }
    }

    /* Right leg wrappers */
    @Override
    public ModelRenderer getBipedRightUpperLeg() {
        return bipedRightLeg;
    }

    @Override
    public ModelRenderer getBipedRightLowerLeg() {
        return bipedRightLowerLeg;
    }

    /* Left leg wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperLeg() {
        return bipedLeftLeg;
    }

    @Override
    public ModelRenderer getBipedLeftLowerLeg() {
        return bipedLeftLowerLeg;
    }

    /* Right arm wrappers */
    @Override
    public ModelRenderer getBipedRightUpperArm() {
        return bipedRightArm;
    }

    @Override
    public ModelRenderer getBipedRightForeArm() {
        return bipedRightForeArm;
    }

    /* Left arm wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperArm() {
        return bipedLeftArm;
    }

    @Override
    public ModelRenderer getBipedLeftForeArm() {
        return bipedLeftForeArm;
    }

    /* Body wrappers */
    @Override
    public ModelRenderer getBipedBody() {
        return bipedBody;
    }

    /* Head wrappers */
    @Override
    public ModelRenderer getBipedHead() {
        return bipedHead;
    }

    @Override
    public ModelRenderer getBipedHeadwear() {
        return bipedHeadwear;
    }
}
