package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class WakandaForeverPostHandler extends AbstractPostCopyAnglesAnimationHandler {


    @Override
    public float modifyState() {
        return 1.0F;
    }


    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {

        player.getBipedRightForeArm().rotateAngleY = (float) Math.toRadians(-70.0f * heldPercent);
        player.getBipedRightForeArm().offsetY = .4F;
        player.getBipedRightForeArm().offsetX = .1F;
        player.getBipedRightForeArm().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);

        player.getBipedRightForeArmwear().rotateAngleY = (float) Math.toRadians(-70.0f * heldPercent);
        player.getBipedRightForeArmwear().offsetY = .4F;
        player.getBipedRightForeArmwear().offsetX = .1F;
        player.getBipedRightForeArmwear().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);


        player.getBipedLeftForeArm().rotateAngleY = (float) Math.toRadians(70.0f * heldPercent);
        player.getBipedLeftForeArm().offsetY = .4F;
        player.getBipedLeftForeArm().offsetX = -.1F;
        player.getBipedLeftForeArm().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);

        player.getBipedLeftForeArmwear().rotateAngleY = (float) Math.toRadians(70.0f * heldPercent);
        player.getBipedLeftForeArmwear().offsetY = .4F;
        player.getBipedLeftForeArmwear().offsetX = -.1F;
        player.getBipedLeftForeArmwear().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);


    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {

        player.getBipedRightForeArm().rotateAngleY = (float) Math.toRadians(-70.0f * heldPercent);
        player.getBipedRightForeArm().offsetY = .4F;
        player.getBipedRightForeArm().offsetX = .1F;
        player.getBipedRightForeArm().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);


        player.getBipedLeftForeArm().rotateAngleY = (float) Math.toRadians(70.0f * heldPercent);
        player.getBipedLeftForeArm().offsetY = .4F;
        player.getBipedLeftForeArm().offsetX = -.1F;
        player.getBipedLeftForeArm().rotateAngleX = (float) Math.toRadians(-135.0f * heldPercent);

    }
}
