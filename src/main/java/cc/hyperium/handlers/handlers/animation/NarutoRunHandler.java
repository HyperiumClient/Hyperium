package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NarutoRunHandler extends AbstractPreCopyAnglesAnimationHandler {
    private Map<UUID, Boolean> sprintStates = new HashMap<>();

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
        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);

        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);

        //player.getBipedBody().rotateAngleX += (float) Math.toRadians(30 * heldPercent);
        //player.getBipedBodywear().rotateAngleX += (float) Math.toRadians(30 * heldPercent);
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(85.0f * heldPercent);
        player.getBipedBody().rotateAngleX += (float) Math.toRadians(30 * heldPercent);
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        for (EntityPlayer p : Minecraft.getMinecraft().theWorld.playerEntities) {
            if (p.isSprinting() != sprintStates.computeIfAbsent(p.getUniqueID(), u -> p.isSprinting())) {
                if (p.isSprinting())
                    startAnimation(p.getUniqueID());
                else
                    stopAnimation(p.getUniqueID());
                sprintStates.put(p.getUniqueID(), p.isSprinting());
            }
        }
    }

    @InvokeEvent
    public void worldChange(WorldChangeEvent e) {
        sprintStates.clear();
    }
}
