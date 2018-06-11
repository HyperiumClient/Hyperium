package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class WakandaForeverPreHandler extends AbstractPreCopyAnglesAnimationHandler {




    @Override
    public float modifyState() {
        return 1.0F;
    }



    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {

        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(30.0f * heldPercent);

        player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(30.0f * heldPercent);

    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-30.0f * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(30.0f * heldPercent);



    }
}
