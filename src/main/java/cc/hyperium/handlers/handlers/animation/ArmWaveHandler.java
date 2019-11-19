package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.utils.model.IModelBiped;
import cc.hyperium.utils.model.IModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class ArmWaveHandler extends AbstractPreCopyAnglesAnimationHandler {

    @Override
    public void onRender() {
        if (!get(Minecraft.getMinecraft().thePlayer.getUniqueID()).isAnimating() && get(Minecraft.getMinecraft().thePlayer.getUniqueID()).shouldReset())
            state = 0.0f;
    }

    private long systemTime;

    @Override
    public float modifyState() {
        if (systemTime == 0) systemTime = Minecraft.getSystemTime();

        while (systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = HyperiumGui.clamp(HyperiumGui.easeOut(state, asc ? 100.0f : 0.0f, 0.01f, 7), 0.0f, 100.0f);

            systemTime += (1000 / 120);
        }

        return state;
    }


    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IModelPlayer player, float heldPercent) {
        player.getBipedRightForeArm().rotateAngleZ = (float) Math.toRadians(-150.0f * heldPercent);
        player.getBipedRightForeArmwear().rotateAngleZ = (float) Math.toRadians(-150.0f * heldPercent);

        player.getBipedRightForeArm().rotateAngleX = 0;
        player.getBipedRightForeArmwear().rotateAngleX = 0;

        player.getBipedRightForeArm().rotateAngleY = 0;
        player.getBipedRightForeArmwear().rotateAngleY = 0;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IModelBiped player, float heldPercent) {
        player.getBipedRightForeArm().rotateAngleZ = (float) Math.toRadians(-150.0f * heldPercent);
        player.getBipedRightForeArm().rotateAngleY = 0;
        player.getBipedRightForeArm().rotateAngleX = 0;
    }
}
