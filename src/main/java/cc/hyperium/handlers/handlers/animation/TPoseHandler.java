package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class TPoseHandler extends AbstractPreCopyAnglesAnimationHandler {

    @Override
    public void onRender() {
        if (!get(Minecraft.getMinecraft().thePlayer.getUniqueID()).isAnimating() && get(Minecraft.getMinecraft().thePlayer.getUniqueID()).shouldReset())
            state = 0.0f;
    }

    @Override
    public float modifyState() {
        return HyperiumGui.clamp(
            HyperiumGui.easeOut(
                this.state,
                100.0f,
                0.01f,
                5
            ),
            0.0f,
            100.0f
        );
    }


    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleX = 0;
        player.getBipedLeftUpperArmwear().rotateAngleX = 0;

        player.getBipedLeftUpperArm().rotateAngleY = 0;
        player.getBipedLeftUpperArmwear().rotateAngleY = 0;

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);

        player.getBipedRightUpperArm().rotateAngleX = 0;
        player.getBipedRightUpperArmwear().rotateAngleX = 0;

        player.getBipedRightUpperArm().rotateAngleY = 0;
        player.getBipedRightUpperArmwear().rotateAngleY = 0;

//        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
//        player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
//
//        player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
//        player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
//
//        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
//
//        player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
//
//        final float rotationX = entity.rotationPitch;
//        final float rotationY = entity.renderYawOffset - entity.rotationYaw;
//
//        player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
//        player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
//
//        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
//        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleY = 0;
        player.getBipedLeftUpperArm().rotateAngleX = 0;

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleY = 0;
        player.getBipedRightUpperArm().rotateAngleX = 0;

//        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
//        player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
//        final float rotationX = entity.rotationPitch;
//        player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
//        final float rotationY = entity.renderYawOffset - entity.rotationYaw;
//        player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
//        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
//        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
    }
}
