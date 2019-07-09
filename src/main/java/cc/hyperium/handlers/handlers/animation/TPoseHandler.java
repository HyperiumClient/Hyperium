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
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleY = 0;
        player.getBipedLeftUpperArm().rotateAngleX = 0;

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleY = 0;
        player.getBipedRightUpperArm().rotateAngleX = 0;
    }
}
