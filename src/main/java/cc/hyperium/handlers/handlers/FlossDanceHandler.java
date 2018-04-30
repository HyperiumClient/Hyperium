package cc.hyperium.handlers.handlers;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cc.hyperium.event.*;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.items.CosmeticSettings;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class FlossDanceHandler {

    // x, y, z values
    private final Random random = new Random();
    private final ConcurrentHashMap<UUID, DanceState> danceStates = new ConcurrentHashMap<>();
    private float state = 0;
    private boolean right = true;
    private boolean asc = true;
    private ArmsDirection armsDirection = ArmsDirection.HORIZONTAL;
    private final float[] randomHeadMovement = new float[3];
    private long systemTime = 0;

    public FlossDanceHandler() {
        fillRandomHeadMovementArray();
    }

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        danceStates.clear();

    }

    @InvokeEvent
    public void onRender(RenderEvent e) {
        danceStates.values().forEach(DanceState::update);

        float speed = CosmeticSettings.flossDanceSpeed * 2;

        if (this.systemTime == 0) this.systemTime = Minecraft.getSystemTime();

        if (this.systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = HyperiumGui.clamp(state + (asc ? speed : -speed), 0.0f, 100.0f);
            this.systemTime += (1000 / 120);

            if (state <= 0) {
                asc = true;
                right = !right;
                armsDirection = ArmsDirection.values()[armsDirection.ordinal() + 1 >= ArmsDirection.values().length ? 0 : armsDirection.ordinal() + 1];
                fillRandomHeadMovementArray();
            } else if (state >= 100) {
                asc = false;
            }
        }
    }

    private void fillRandomHeadMovementArray() {
        randomHeadMovement[0] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
        randomHeadMovement[1] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
        randomHeadMovement[2] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
    }

    public DanceState get(UUID uuid) {
        return danceStates.computeIfAbsent(uuid, DanceState::new);
    }

    public void startDancing(UUID uuid) {
        get(uuid).ensureDancingFor(60);
    }

    public void stopDancing(UUID uuid) {
        get(uuid).setToggled(false);
        get(uuid).stopDancing();
    }

    @InvokeEvent
    public void onPreCopyPlayerModelAngles(PreCopyPlayerModelAnglesEvent event) {
        AbstractClientPlayer entity = event.getEntity();
        IMixinModelBiped player = event.getModel();

		if (player instanceof IMixinModelPlayer) {
			modify(entity, ((IMixinModelPlayer) player));
		} else {
			modify(entity, player);
		}
    }

    public void modify(AbstractClientPlayer entity, IMixinModelPlayer player) {
        DanceState danceState = get(entity.getUniqueID());
        int ticks = danceState.danceFrames;
        player.getBipedBody().rotateAngleZ = 0F;
        player.getBipedBodywear().rotateAngleZ = 0F;

        player.getBipedRightUpperLeg().rotateAngleZ = 0F;
        player.getBipedRightUpperLegwear().rotateAngleZ = 0F;
        player.getBipedLeftUpperLeg().rotateAngleZ = 0F;
        player.getBipedLeftUpperLegwear().rotateAngleZ = 0F;
        player.getBipedRightUpperLeg().offsetX = 0F;
        player.getBipedRightUpperLegwear().offsetX = 0F;
        player.getBipedLeftUpperLeg().offsetX = 0F;
        player.getBipedLeftUpperLegwear().offsetX = 0F;
        player.getBipedHead().rotateAngleZ = 0F;
        player.getBipedHeadwear().rotateAngleZ = 0F;

        if (ticks <= 2) {
            if (danceState.shouldReset()) {
                resetAnimation(player, true);
                player.getBipedBodywear().rotateAngleZ = 0;
                player.getBipedBodywear().rotateAngleX = 0;
                player.getBipedBodywear().rotateAngleY = 0;
                player.getBipedLeftUpperLegwear().rotateAngleZ = 0;
                player.getBipedRightUpperLegwear().rotateAngleZ = 0;

                player.getBipedBodywear().offsetX = 0;
                player.getBipedRightUpperLegwear().offsetX = 0;
                player.getBipedLeftUpperLegwear().offsetX = 0;
            }

            return;
        }

        float heldPercent = state / 100F;

        player.getBipedRightUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedRightUpperLegwear().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedLeftUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedLeftUpperLegwear().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedRightUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedRightUpperLegwear().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedLeftUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedLeftUpperLegwear().offsetX = (right ? -0.17f : 0.17f) * heldPercent;

        player.getBipedHead().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHead().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHead().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
        player.getBipedHeadwear().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
    }

    public void modify(AbstractClientPlayer entity, IMixinModelBiped player) {
        DanceState danceState = get(entity.getUniqueID());
        int ticks = danceState.danceFrames;

        if (ticks <= 2) {
            if (danceState.shouldReset()) {
                resetAnimation(player, true);
            }

            return;
        }

        float heldPercent = state / 100F;
        float offset = -0.4F;
        player.getBipedRightUpperLeg().rotateAngleX = (float) Math.toRadians((right ? 30.0F : -30.0F) * heldPercent);
        player.getBipedRightLowerLeg().rotateAngleX = (float) Math.toRadians((right ? -30.0F : 30.0F) * heldPercent);

        player.getBipedRightLowerLeg().offsetZ = (right ? -offset : offset) * heldPercent;

        player.getBipedLeftUpperLeg().rotateAngleX = (float) Math.toRadians((right ? -30.0F : 30.0F) * heldPercent);
        player.getBipedLeftLowerLeg().rotateAngleX = (float) Math.toRadians((right ? 30.0F : -30.0F) * heldPercent);

        player.getBipedLeftLowerLeg().offsetZ = (right ? offset : -offset) * heldPercent;

        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians((right ? 30.0F : -30.0F) * heldPercent);
        player.getBipedRightForeArm().rotateAngleX = (float) Math.toRadians((right ? -30.0F : 30.0F) * heldPercent);

        offset = 0;

        player.getBipedRightForeArm().offsetZ = (right ? -offset : offset) * heldPercent;

        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians((right ? -30.0F : 30.0F) * heldPercent);
        player.getBipedLeftForeArm().rotateAngleX = (float) Math.toRadians((right ? 30.0F : -30.0F) * heldPercent);

        player.getBipedLeftForeArm().offsetZ = (right ? offset : -offset) * heldPercent;

//        player.getBipedBody().rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);
//
//        player.getBipedRightUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
//        player.getBipedLeftUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
//        player.getBipedRightUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
//        player.getBipedLeftUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
//
//        player.getBipedHead().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
//        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
//        player.getBipedHead().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
//        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
//        player.getBipedHead().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
//        player.getBipedHeadwear().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
//
//        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
//        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
//
//        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
//        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
    }

    private void resetAnimation(IMixinModelBiped player, boolean head) {
        if (head) {
            player.getBipedHead().rotateAngleX = 0;
            player.getBipedHeadwear().rotateAngleX = 0;
            player.getBipedHead().rotateAngleY = 0;
            player.getBipedHeadwear().rotateAngleY = 0;
            player.getBipedHead().rotateAngleZ = 0;
            player.getBipedHeadwear().rotateAngleZ = 0;
        }

        player.getBipedBody().rotateAngleZ = 0;
        player.getBipedBody().rotateAngleX = 0;
        player.getBipedBody().rotateAngleY = 0;

        player.getBipedRightUpperLeg().rotateAngleZ = 0;
        player.getBipedLeftUpperLeg().rotateAngleZ = 0;
        player.getBipedRightUpperLeg().offsetX = 0;
        player.getBipedLeftUpperLeg().offsetX = 0;


        player.getBipedRightUpperArm().rotateAngleZ = 0;
        player.getBipedRightUpperArm().rotateAngleX = 0;

        player.getBipedLeftUpperArm().rotateAngleZ = 0;
        player.getBipedLeftUpperArm().rotateAngleX = 0;

        player.getBipedBody().rotateAngleZ = 0;
        player.getBipedLeftUpperLeg().rotateAngleZ = 0;
        player.getBipedRightUpperLeg().rotateAngleZ = 0;

        player.getBipedRightUpperLeg().offsetX = 0;
        player.getBipedLeftUpperLeg().offsetX = 0;
    }

    enum ArmsDirection {
        HORIZONTAL, BACK, FRONT
    }

    public class DanceState {
        boolean reset;
        private final UUID uuid;
        private int danceFrames = 0;
        private long systemTime;
        private boolean toggled;

        public DanceState(UUID uuid) {
            this.uuid = uuid;
            this.systemTime = Minecraft.getSystemTime();
            this.toggled = false;
            reset = true;
        }

        private void update() {
            while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                this.danceFrames--;
                this.systemTime += (1000 / 60);
            }

            if (danceFrames < 0) {
                if (this.toggled) {
                    ensureDancingFor(60);
                } else {
                    danceFrames = -1;
                }
            }
        }

        public void ensureDancingFor(int seconds) {
            danceFrames = Math.max(danceFrames, seconds * 60);
            reset = true;
        }

        public void stopDancing() {
            danceFrames = 1;
        }

        public boolean isDancing() {
            return danceFrames > 0;
        }

        public void setToggled(boolean toggled) {
            this.toggled = toggled;
        }

        public boolean shouldReset() {
            return this.danceFrames == 1;
        }
    }
}
