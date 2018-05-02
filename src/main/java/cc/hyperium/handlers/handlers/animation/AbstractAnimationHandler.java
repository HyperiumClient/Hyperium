package cc.hyperium.handlers.handlers.animation;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cc.hyperium.event.*;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;

public abstract class AbstractAnimationHandler<T extends CopyPlayerModelAnglesEvent> {

    private final ConcurrentHashMap<UUID, AnimationState> animationStates = new ConcurrentHashMap<>();

    protected float state = 0;
    protected boolean right = true;
    protected boolean asc = true;
    private long systemTime = 0;

    @InvokeEvent
    public void onCopyPlayerModelAngles(T event) {
        AbstractClientPlayer entity = event.getEntity();
        IMixinModelBiped player = event.getModel();

        modify(entity, player);
    }

    @InvokeEvent
    public void onRender(RenderEvent e) {
        animationStates.values().forEach(AnimationState::update);

        if (this.systemTime == 0) this.systemTime = Minecraft.getSystemTime();

        if (this.systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = modifyState(state);

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

    public void onStartOfState() {
    }

    public abstract float modifyState(float currentState);

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

    private void modify(AbstractClientPlayer entity, IMixinModelBiped player) {
        AnimationState danceState = get(entity.getUniqueID());
        int ticks = danceState.frames;
        resetAnimation(player);

        if (ticks <= 2) {
            if (danceState.shouldReset()) {
                resetAnimation(player);
            }

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

    private void resetAnimation(IMixinModelBiped player) {
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
        private boolean reset;
        private int frames = 0;
        private long systemTime;
        private boolean toggled;

        public AnimationState() {
            this.systemTime = Minecraft.getSystemTime();
            this.toggled = false;
            this.reset = true;
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
            reset = true;
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
    }
}
