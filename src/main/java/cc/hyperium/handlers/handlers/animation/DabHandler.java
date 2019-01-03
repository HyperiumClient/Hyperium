package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class DabHandler extends AbstractPreCopyAnglesAnimationHandler {

    private int dabs;

    @Override
    public void onRender() {
        if (state == 100 && get(Minecraft.getMinecraft().thePlayer.getUniqueID()).isAnimating())
            incDabs();
    }

    @Override
    public float modifyState() {
        return HyperiumGui.clamp(
            HyperiumGui.easeOut(
                this.state,
                this.asc ? 100.0f : 0.0f,
                0.01f,
                5
            ),
            0.0f,
            100.0f
        );
    }

    public int getDabs() {
        return dabs;
    }

    public void incDabs() {
        dabs++;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {

        if (right) {
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

            player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);

            player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);

            final float rotationX = entity.rotationPitch;
            final float rotationY = entity.renderYawOffset - entity.rotationYaw;

            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);

            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);

        } else {
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);

            player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);

            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);

            player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);


            final float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            final float rotationY = entity.renderYawOffset - entity.rotationYaw;

            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);

            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);

        }
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        if (right) {
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
            final float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            final float rotationY = entity.renderYawOffset - entity.rotationYaw;
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
        } else {
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);
            final float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            final float rotationY = entity.renderYawOffset - entity.rotationYaw;
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);
            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
        }
    }
}
