package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAnimationHandler {

    private final ConcurrentHashMap<UUID, AnimationState> animationStates = new ConcurrentHashMap<>();

    protected float state = 0;
    protected boolean right = true;
    protected boolean asc = true;
    private long systemTime = 0;

    @InvokeEvent
    public void onRender(RenderEvent e) {
        animationStates.values().forEach(AnimationState::update);

        onRender();

        if (this.systemTime == 0) this.systemTime = Minecraft.getSystemTime();

        if (this.systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = modifyState();

            this.systemTime += (1000 / 120);

            if (state <= 0) {
                asc = true;
                right = !right;
                onStartOfState();
            } else if (state >= 100) {
                asc = false;
            }
        }
    }

    public void onRender() {
    }

    public void onStartOfState() {
    }

    public abstract float modifyState();

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        animationStates.clear();
    }

    public AnimationState get(UUID uuid) {
        return animationStates.computeIfAbsent(uuid, r -> new AnimationState());
    }

    public void startAnimation(UUID uuid) {
        get(uuid).ensureAnimationFor(60);
    }

    public void stopAnimation(UUID uuid) {
        get(uuid).setToggled(false);
        get(uuid).stopAnimation();
    }

    protected void modify(AbstractClientPlayer entity, IMixinModelBiped player) {
        AnimationState animationState = get(entity.getUniqueID());
        int ticks = animationState.frames;
        player.getBipedBody().rotateAngleZ = 0F;
        player.getBipedRightUpperLeg().rotateAngleZ = 0F;
        player.getBipedLeftUpperLeg().rotateAngleZ = 0F;
        player.getBipedRightUpperLeg().offsetX = 0F;
        player.getBipedLeftUpperLeg().offsetX = 0F;
        player.getBipedHead().rotateAngleZ = 0F;
        player.getBipedHeadwear().rotateAngleZ = 0F;
        if (player instanceof IMixinModelPlayer) {
            IMixinModelPlayer player1 = (IMixinModelPlayer) player;
            player1.getBipedBodywear().rotateAngleZ = 0F;
            player1.getBipedRightUpperLegwear().rotateAngleZ = 0F;
            player1.getBipedLeftUpperLegwear().rotateAngleZ = 0F;
            player1.getBipedRightUpperLegwear().offsetX = 0F;
            player1.getBipedLeftUpperLegwear().offsetX = 0F;

        }
        if (ticks <= 0) {

            return;
        }

        float heldPercent = state / 100F;

        if (player instanceof IMixinModelPlayer) {
            modifyPlayer(entity, ((IMixinModelPlayer) player), heldPercent);
        } else {
            modifyPlayer(entity, player, heldPercent);
        }

    }

    public abstract void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent);

    public abstract void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent);

    protected void resetAnimation(IMixinModelBiped player) {
        resetModelRenderers(
                player.getBipedHead(), player.getBipedHeadwear(), //
                player.getBipedBody(), //
                player.getBipedRightUpperLeg(), player.getBipedRightLowerLeg(), //
                player.getBipedLeftUpperLeg(), player.getBipedLeftLowerLeg(), //
                player.getBipedRightUpperArm(), player.getBipedRightForeArm(), //
                player.getBipedLeftUpperArm(), player.getBipedLeftForeArm() //
        );

        if (player instanceof IMixinModelPlayer) {
            IMixinModelPlayer modelPlayer = (IMixinModelPlayer) player;
            resetModelRenderers(
                    modelPlayer.getBipedBodywear(), //
                    modelPlayer.getBipedRightUpperLegwear(), modelPlayer.getBipedRightLowerLegwear(), //
                    modelPlayer.getBipedLeftUpperLegwear(), modelPlayer.getBipedLeftLowerLegwear(), //
                    modelPlayer.getBipedRightUpperArmwear(), modelPlayer.getBipedRightForeArmwear(), //
                    modelPlayer.getBipedLeftUpperArmwear(), modelPlayer.getBipedLeftForeArmwear() //
            );
        }
    }

    private void resetModelRenderers(ModelRenderer... renderers) {
        for (ModelRenderer renderer : renderers) {
            renderer.rotateAngleX = 0;
            renderer.rotateAngleY = 0;
            renderer.rotateAngleZ = 0;

            renderer.offsetX = 0;
            renderer.offsetY = 0;
            renderer.offsetZ = 0;
        }
    }

    public class AnimationState {
        private int frames = 0;
        private long systemTime;
        private boolean toggled;

        public AnimationState() {
            this.systemTime = Minecraft.getSystemTime();
            this.toggled = false;
        }

        private void update() {
            while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
                this.frames--;
                this.systemTime += (1000 / 60);
            }

            if (frames < 0) {
                if (this.toggled) {
                    ensureAnimationFor(60);
                } else {
                    frames = -1;
                }
            }
        }

        public void ensureAnimationFor(int seconds) {
            frames = Math.max(frames, seconds * 60);
        }

        public void stopAnimation() {
            frames = 1;
        }

        public boolean isAnimating() {
            return frames > 0;
        }

        public void setToggled(boolean toggled) {
            this.toggled = toggled;
        }

        public boolean shouldReset() {
            return this.frames == 1;
        }

        public int getFrames() {
            return frames;
        }
    }
}
