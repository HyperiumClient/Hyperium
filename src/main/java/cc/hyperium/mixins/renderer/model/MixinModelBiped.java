package cc.hyperium.mixins.renderer.model;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBox;
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

@Mixin(ModelBiped.class)
public class MixinModelBiped extends ModelBase implements IMixinModelBiped {

    @Shadow
    public ModelRenderer bipedHead;

    @Shadow
    public ModelRenderer bipedHeadwear;

    @Shadow
    public ModelRenderer bipedBody;

    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public ModelRenderer bipedLeftArm;

    @Shadow
    public ModelRenderer bipedRightLeg;

    @Shadow
    public ModelRenderer bipedLeftLeg;

    @Shadow
    public int heldItemLeft;

    @Shadow
    public int heldItemRight;

    @Shadow
    public boolean isSneak;

    @Shadow
    public boolean aimedBow;

    protected ModelRenderer bipedLeftForeArm;
    protected ModelRenderer bipedRightForeArm;
    protected ModelRenderer bipedLeftLowerLeg;
    protected ModelRenderer bipedRightLowerLeg;

    @Inject(method = "<init>(FFII)V", at = @At("RETURN"))
    private void injectModelChanges(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn, CallbackInfo ci) {
        // Doing these checks because other ModelBiped (ModelZombie, ModelArmorStand etc) were messed up
        // The only ModelBiped which isn't extended is armor as far as I know. If not the check should be more specific for armor.
        if (getClass().equals(ModelBiped.class)) {
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
            this.bipedRightForeArm = new ModelRenderer(this, 40, 22);
            this.bipedRightForeArm.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedRightForeArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
            this.bipedLeftArm = new ModelRenderer(this, 40, 16);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
            this.bipedLeftForeArm = new ModelRenderer(this, 40, 22);
            this.bipedLeftForeArm.mirror = true;
            this.bipedLeftForeArm.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedLeftForeArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);

            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
            this.bipedRightLowerLeg = new ModelRenderer(this, 0, 22);
            this.bipedRightLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedRightLowerLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);


            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
            this.bipedLeftLowerLeg = new ModelRenderer(this, 0, 22);
            this.bipedLeftLowerLeg.mirror = true;
            this.bipedLeftLowerLeg.addBox(-2.0F, 6.0F, -2.0F, 4, 6, 4, modelSize);
            this.bipedLeftLowerLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);


            fixTopAndBottomOfLimbWrongTextures(this.bipedLeftForeArm, this.bipedRightForeArm, this.bipedLeftLowerLeg,
                this.bipedRightLowerLeg);
        }
    }

    /**
     * Fixes boxes having the wrong textures, when they are the second part of a limb
     *
     * @param models The {@link ModelRenderer} parts you want to fix
     */
    protected void fixTopAndBottomOfLimbWrongTextures(ModelRenderer... models) {
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
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
        GlStateManager.pushMatrix();
        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);


            // Adding our parts
            if (getClass().equals(ModelBiped.class))
                renderBiped(scale);
        } else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);


            // Adding our parts
            if (getClass().equals(ModelBiped.class))
                renderBiped(scale);
        }

        GlStateManager.popMatrix();
    }

    private void renderBiped(float scale) {
        this.bipedLeftForeArm.render(scale);
        this.bipedRightForeArm.render(scale);
        this.bipedLeftLowerLeg.render(scale);
        this.bipedRightLowerLeg.render(scale);
    }

    /**
     * @author Amplifiable
     * @reason 1.7 Blocking & Item Held
     */
    @Overwrite
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.bipedHead.rotateAngleY = netHeadYaw / 57.295776f;
        this.bipedHead.rotateAngleX = headPitch / 57.295776f;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 2.0f * limbSwingAmount * 0.5f;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + 3.1415927f) * 1.4f * limbSwingAmount;
        this.bipedRightLeg.rotateAngleY = 0.0f;
        this.bipedLeftLeg.rotateAngleY = 0.0f;
        if (this.isRiding) {
            final ModelRenderer bipedRightArm = this.bipedRightArm;
            bipedRightArm.rotateAngleX -= 0.62831855f;
            final ModelRenderer bipedLeftArm = this.bipedLeftArm;
            bipedLeftArm.rotateAngleX -= 0.62831855f;
            this.bipedRightLeg.rotateAngleX = -1.2566371f;
            this.bipedLeftLeg.rotateAngleX = -1.2566371f;
            this.bipedRightLeg.rotateAngleY = 0.31415927f;
            this.bipedLeftLeg.rotateAngleY = -0.31415927f;
        }
        if (this.heldItemLeft != 0) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5f - 0.31415927f * this.heldItemLeft;
        }
        this.bipedRightArm.rotateAngleY = 0.0f;
        this.bipedRightArm.rotateAngleZ = 0.0f;
        switch (this.heldItemRight) {
            case 1: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * this.heldItemRight;
                break;
            }
            case 3: {
                this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5f - 0.31415927f * this.heldItemRight;
                if (!Settings.OLD_BLOCKING) {
                    this.bipedRightArm.rotateAngleY = -0.5235988f;
                    break;
                }
                break;
            }
        }
        this.bipedLeftArm.rotateAngleY = 0.0f;
        if (this.swingProgress > -9990.0f) {
            float f = this.swingProgress;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f) * 3.1415927f * 2.0f) * 0.2f;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0f;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0f;
            final ModelRenderer bipedRightArm2 = this.bipedRightArm;
            bipedRightArm2.rotateAngleY += this.bipedBody.rotateAngleY;
            final ModelRenderer bipedLeftArm2 = this.bipedLeftArm;
            bipedLeftArm2.rotateAngleY += this.bipedBody.rotateAngleY;
            final ModelRenderer bipedLeftArm3 = this.bipedLeftArm;
            bipedLeftArm3.rotateAngleX += this.bipedBody.rotateAngleY;
            f = 1.0f - this.swingProgress;
            f *= f;
            f *= f;
            f = 1.0f - f;
            final float f2 = MathHelper.sin(f * 3.1415927f);
            final float f3 = MathHelper.sin(this.swingProgress * 3.1415927f) * -(this.bipedHead.rotateAngleX - 0.7f) * 0.75f;
            this.bipedRightArm.rotateAngleX -= (float) (f2 * 1.2 + f3);
            final ModelRenderer bipedRightArm3 = this.bipedRightArm;
            bipedRightArm3.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
            final ModelRenderer bipedRightArm4 = this.bipedRightArm;
            bipedRightArm4.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927f) * -0.4f;
        }
        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5f;
            final ModelRenderer bipedRightArm5 = this.bipedRightArm;
            bipedRightArm5.rotateAngleX += 0.4f;
            final ModelRenderer bipedLeftArm4 = this.bipedLeftArm;
            bipedLeftArm4.rotateAngleX += 0.4f;
            this.bipedRightLeg.rotationPointZ = 4.0f;
            this.bipedLeftLeg.rotationPointZ = 4.0f;
            this.bipedRightLeg.rotationPointY = 9.0f;
            this.bipedLeftLeg.rotationPointY = 9.0f;
            this.bipedHead.rotationPointY = 1.0f;
        } else {
            this.bipedBody.rotateAngleX = 0.0f;
            this.bipedRightLeg.rotationPointZ = 0.1f;
            this.bipedLeftLeg.rotationPointZ = 0.1f;
            this.bipedRightLeg.rotationPointY = 12.0f;
            this.bipedLeftLeg.rotationPointY = 12.0f;
            this.bipedHead.rotationPointY = 0.0f;
        }
        final ModelRenderer bipedRightArm6 = this.bipedRightArm;
        bipedRightArm6.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedLeftArm5 = this.bipedLeftArm;
        bipedLeftArm5.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer bipedRightArm7 = this.bipedRightArm;
        bipedRightArm7.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        final ModelRenderer bipedLeftArm6 = this.bipedLeftArm;
        bipedLeftArm6.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        if (this.aimedBow) {
            final float f4 = 0.0f;
            final float f5 = 0.0f;
            this.bipedRightArm.rotateAngleZ = 0.0f;
            this.bipedLeftArm.rotateAngleZ = 0.0f;
            this.bipedRightArm.rotateAngleY = -(0.1f - f4 * 0.6f) + this.bipedHead.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = 0.1f - f4 * 0.6f + this.bipedHead.rotateAngleY + 0.4f;
            this.bipedRightArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            this.bipedLeftArm.rotateAngleX = -1.5707964f + this.bipedHead.rotateAngleX;
            final ModelRenderer bipedRightArm8 = this.bipedRightArm;
            bipedRightArm8.rotateAngleX -= f4 * 1.2f - f5 * 0.4f;
            final ModelRenderer bipedLeftArm7 = this.bipedLeftArm;
            bipedLeftArm7.rotateAngleX -= f4 * 1.2f - f5 * 0.4f;
            final ModelRenderer bipedRightArm9 = this.bipedRightArm;
            bipedRightArm9.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer bipedLeftArm8 = this.bipedLeftArm;
            bipedLeftArm8.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09f) * 0.05f + 0.05f;
            final ModelRenderer bipedRightArm10 = this.bipedRightArm;
            bipedRightArm10.rotateAngleX += MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
            final ModelRenderer bipedLeftArm9 = this.bipedLeftArm;
            bipedLeftArm9.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067f) * 0.05f;
        }
        copyModelAngles(this.bipedHead, this.bipedHeadwear);
        if (getClass().equals(ModelBiped.class)) {
            boolean isPlayer = entityIn instanceof AbstractClientPlayer;
            if (isPlayer) {
                EventBus.INSTANCE.post(new PreCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
            }

            copyModelAnglesAndOffest(this.bipedLeftArm, this.bipedLeftForeArm);
            copyModelAnglesAndOffest(this.bipedRightArm, this.bipedRightForeArm);

            copyModelAnglesAndOffest(this.bipedLeftLeg, this.bipedLeftLowerLeg);
            copyModelAnglesAndOffest(this.bipedRightLeg, this.bipedRightLowerLeg);

            if (isPlayer) {
                EventBus.INSTANCE.post(new PostCopyPlayerModelAnglesEvent(((AbstractClientPlayer) entityIn), this));
            }
        }
    }

    protected void copyModelAnglesAndOffest(ModelRenderer src, ModelRenderer dest) {
        copyModelAngles(src, dest);
        dest.offsetX = src.offsetX;
        dest.offsetY = src.offsetY;
        dest.offsetZ = src.offsetZ;
    }

    @Inject(method = "setInvisible", at = @At("RETURN"))
    private void setInvisible(boolean invisible, CallbackInfo ci) {
        if (getClass().equals(ModelBiped.class)) {
            this.bipedLeftForeArm.showModel = invisible;
            this.bipedRightForeArm.showModel = invisible;
            this.bipedLeftLowerLeg.showModel = invisible;
            this.bipedRightLowerLeg.showModel = invisible;
        }
    }

    /* Right leg wrappers */
    @Override
    public ModelRenderer getBipedRightUpperLeg() {
        return this.bipedRightLeg;
    }

    @Override
    public ModelRenderer getBipedRightLowerLeg() {
        return this.bipedRightLowerLeg;
    }

    /* Left leg wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperLeg() {
        return this.bipedLeftLeg;
    }

    @Override
    public ModelRenderer getBipedLeftLowerLeg() {
        return this.bipedLeftLowerLeg;
    }

    /* Right arm wrappers */
    @Override
    public ModelRenderer getBipedRightUpperArm() {
        return this.bipedRightArm;
    }

    @Override
    public ModelRenderer getBipedRightForeArm() {
        return this.bipedRightForeArm;
    }

    /* Left arm wrappers */
    @Override
    public ModelRenderer getBipedLeftUpperArm() {
        return this.bipedLeftArm;
    }

    @Override
    public ModelRenderer getBipedLeftForeArm() {
        return this.bipedLeftForeArm;
    }

    /* Body wrappers */
    @Override
    public ModelRenderer getBipedBody() {
        return this.bipedBody;
    }

    /* Head wrappers */
    @Override
    public ModelRenderer getBipedHead() {
        return this.bipedHead;
    }

    @Override
    public ModelRenderer getBipedHeadwear() {
        return this.bipedHeadwear;
    }
}
