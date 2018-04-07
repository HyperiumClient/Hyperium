package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DabHandler {


    @ConfigOpt
    public int speed = 8;
    private int dabs;
    private ConcurrentHashMap<UUID, DabState> dabStates = new ConcurrentHashMap<>();
    private float state = 0;
    private boolean right = true;
    private boolean asc = true;

    @InvokeEvent
    public void tick(TickEvent event) {
        dabStates.values().forEach(DabState::tick);
        speed = 20;
        state = state + (asc ? speed : -speed);
        if (state <= 0) {
            state = 0;
            asc = true;
            right = !right;
        }
        if (state >= 100) {
            state = 100;
            asc = false;
        }

    }

    public int getDabs() {
        return dabs;
    }

    public void incDabs() {
        dabs++;
    }

    public DabState get(UUID uuid) {
        return dabStates.computeIfAbsent(uuid, DabState::new);
    }

    public void startDabbing(UUID uuid) {
        get(uuid).ensureDabbingFor(60);
    }

    public void stopDabbing(UUID uuid) {
        get(uuid).stopDabbing();
    }

    public void modify(AbstractClientPlayer entity, ModelPlayer player) {
        int ticks = get(entity.getUniqueID()).dabTicks;
        if (ticks != 0) {
            float heldPercent = state / 100F;
            if (right) {
                player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedRightArm.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
                player.bipedRightArmwear.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
                player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArm.rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
                player.bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArmwear.rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
                final float rotationX = entity.rotationPitch;
                player.bipedHead.rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
                final float rotationY = entity.renderYawOffset - entity.rotationYaw;
                player.bipedHead.rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
                player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
                player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
            } else {
                player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedLeftArm.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
                player.bipedLeftArmwear.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedLeftArmwear.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
                player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArm.rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArm.rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);
                player.bipedRightArmwear.rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArmwear.rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArmwear.rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);
                final float rotationX = entity.rotationPitch;
                player.bipedHead.rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
                final float rotationY = entity.renderYawOffset - entity.rotationYaw;
                player.bipedHead.rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);
                player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
                player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
            }


        }
    }
    public void modify(AbstractClientPlayer entity, ModelBiped player) {
        int ticks = get(entity.getUniqueID()).dabTicks;
        if (ticks != 0) {
            float heldPercent = state / 100F;
            if (right) {
                player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedRightArm.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
                player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArm.rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
                player.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
                final float rotationX = entity.rotationPitch;
                player.bipedHead.rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
                final float rotationY = entity.renderYawOffset - entity.rotationYaw;
                player.bipedHead.rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
                player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
                player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
            } else {
                player.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
                player.bipedLeftArm.rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
                player.bipedRightArm.rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArm.rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
                player.bipedRightArm.rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);
                final float rotationX = entity.rotationPitch;
                player.bipedHead.rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
                final float rotationY = entity.renderYawOffset - entity.rotationYaw;
                player.bipedHead.rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);
                player.bipedHeadwear.rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
                player.bipedHeadwear.rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
            }


        }
    }
    public class DabState {
        UUID uuid;
        int dabTicks = 0;

        public DabState(UUID uuid) {
            this.uuid = uuid;
        }

        void tick() {
            if (dabTicks > 0)
                dabTicks--;
        }

        public void ensureDabbingFor(int seconds) {
            seconds *= 20;

            dabTicks = Math.max(dabTicks, seconds);

        }

        public void stopDabbing() {
            dabTicks = 0;
        }

    }
}
