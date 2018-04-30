package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.settings.items.CosmeticSettings;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DabHandler {

    private final Map<UUID, DabState> dabStates = new ConcurrentHashMap<>();
    private int dabs;
    private float state = 0;
    private boolean right = true;
    private boolean asc = true;
    private long systemTime = 0;

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        dabStates.clear();
    }

    @InvokeEvent
    public void onRender(RenderEvent e) {
        dabStates.values().forEach(DabState::update);

        if (state == 100 && get(Minecraft.getMinecraft().thePlayer.getUniqueID()).isDabbing())
            incDabs();

        if (this.systemTime == 0) this.systemTime = Minecraft.getSystemTime();

        if (this.systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = HyperiumGui.clamp(
                    HyperiumGui.easeOut(
                            this.state,
                            this.asc ? 100.0f : 0.0f,
                            0.01f,
                            CosmeticSettings.dabSpeed
                    ),
                    0.0f,
                    100.0f
            );

            this.systemTime += (1000 / 120);

            if (state <= 0) {
                asc = true;
                right = !right;
            } else if (state >= 100) {
                asc = false;
            }
        }
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
        get(uuid).setToggled(false);
        get(uuid).stopDabbing();
    }

    public void modify(AbstractClientPlayer entity, IMixinModelPlayer player) {
        int ticks = get(entity.getUniqueID()).dabFrames;

        if (ticks <= 0) return;

        float heldPercent = state / 100F;
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
            player.getBipedHead().rotateAngleX =
                    (float) Math.toRadians(-rotationX * heldPercent) +
                            (float) Math.toRadians(45.0f * heldPercent + rotationX);
            final float rotationY = entity.renderYawOffset - entity.rotationYaw;

            player.getBipedHead().rotateAngleY =
                    (float) Math.toRadians(rotationY * heldPercent) +
                            (float) Math.toRadians(-35.0f * heldPercent - rotationY);

            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);

        }
    }

    public void modify(AbstractClientPlayer entity, IMixinModelBiped player) {
        int ticks = get(entity.getUniqueID()).dabFrames;

        if (ticks <= 0) return;

        float heldPercent = state / 100F;
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

    public class DabState {
        final UUID uuid;
        int dabFrames = 0;
        long systemTime;
        boolean toggled;

        public DabState(UUID uuid) {
            this.uuid = uuid;
            this.systemTime = Minecraft.getSystemTime();
            this.toggled = false;
        }

        void update() {
            while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                this.dabFrames--;
                this.systemTime += (1000 / 60);
            }

            if (dabFrames <= 0) {
                if (CosmeticSettings.dabToggle && this.toggled) {
                    ensureDabbingFor(60);
                } else {
                    dabFrames = 0;
                }
            }
        }

        public void ensureDabbingFor(int seconds) {
            seconds *= 60;

            dabFrames = Math.max(dabFrames, seconds);
        }

        public void stopDabbing() {
            dabFrames = 0;
        }

        public boolean isDabbing() {
            return dabFrames > 0;
        }

        public void setToggled(boolean toggled) {
            this.toggled = toggled;
        }
    }
}
