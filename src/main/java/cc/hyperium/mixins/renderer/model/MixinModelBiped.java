package cc.hyperium.mixins.renderer.model;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBox;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
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

            fixTopAndBottomOfLimbWrongTextures(this.bipedLeftForeArm, this.bipedRightForeArm, this.bipedLeftLowerLeg, this.bipedRightLowerLeg);
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
            if (getClass().equals(ModelBiped.class)) {
                this.bipedLeftForeArm.render(scale);
                this.bipedRightForeArm.render(scale);
                this.bipedLeftLowerLeg.render(scale);
                this.bipedRightLowerLeg.render(scale);
            }
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
            if (getClass().equals(ModelBiped.class)) {
                this.bipedLeftForeArm.render(scale);
                this.bipedRightForeArm.render(scale);
                this.bipedLeftLowerLeg.render(scale);
                this.bipedRightLowerLeg.render(scale);
            }
        }

        GlStateManager.popMatrix();
    }

    @Inject(method =  "setRotationAngles", at = @At("RETURN"))
    private void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn, CallbackInfo ci) {
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
