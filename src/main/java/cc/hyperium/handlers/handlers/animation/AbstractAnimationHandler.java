package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.CosmeticsUtil;
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


    public static boolean reset = false;
    private final ConcurrentHashMap<UUID, AnimationState> animationStates = new ConcurrentHashMap<>();

    protected float state = 0;
    protected boolean right = true;
    protected boolean asc = true;
    private long systemTime = 0;

    @InvokeEvent
    public void onRender(RenderEvent e) {

        long systemTime = Minecraft.getSystemTime();
        animationStates.values().forEach(animationState -> {
            animationState.update(systemTime);
        });

        onRender();

        long systemTime1 = System.currentTimeMillis();
        if (this.systemTime == 0) {
            this.systemTime = systemTime1;
        }

        int msPerTick = 1000 / 120;
        if (this.systemTime < systemTime1 + msPerTick) {
            this.systemTime = systemTime1 + msPerTick;
            state = modifyState();
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

    protected void modify(AbstractClientPlayer entity, IMixinModelBiped player, boolean pre) {
        if (Settings.DISABLE_DANCES) {
            if (!reset) {
                resetAnimation(player);
                reset = true;
            }
            return;
        } else reset = false;
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
            if (pre) {
                player.getBipedBody().offsetZ = 0;
                player.getBipedLeftLowerLeg().offsetZ = 0;
                player.getBipedRightLowerLeg().offsetZ = 0;
                player.getBipedRightUpperLeg().offsetZ = 0;
                player.getBipedLeftUpperLeg().offsetZ = 0;
                player.getBipedRightUpperArm().offsetZ = 0;
                player.getBipedLeftUpperArm().offsetZ = 0;

                player.getBipedBody().offsetY = 0;
                player.getBipedLeftLowerLeg().offsetY = 0;
                player.getBipedRightLowerLeg().offsetY = 0;
                player.getBipedRightUpperLeg().offsetY = 0;
                player.getBipedLeftUpperLeg().offsetY = 0;
                player.getBipedRightUpperArm().offsetY = 0;
                player.getBipedLeftUpperArm().offsetY = 0;


                player.getBipedHead().offsetZ = 0;
                player.getBipedHead().offsetY = 0;
                player.getBipedHeadwear().offsetZ = 0;
                player.getBipedHeadwear().offsetY = 0;
                player.getBipedRightForeArm().offsetY = 0;
                player.getBipedRightForeArm().offsetX = 0;

                if (player instanceof IMixinModelPlayer) {
                    IMixinModelPlayer player1 = (IMixinModelPlayer) player;
                    player1.getBipedLeftUpperArmwear().offsetY = 0;
                    player1.getBipedRightUpperArmwear().offsetY = 0;
                    player1.getBipedBodywear().offsetY = 0;
                    player1.getBipedLeftUpperLegwear().offsetY = 0;
                    player1.getBipedLeftLowerLegwear().offsetY = 0;
                    player1.getBipedRightUpperLegwear().offsetY = 0;
                    player1.getBipedRightLowerLegwear().offsetY = 0;


                    player1.getBipedLeftUpperArmwear().offsetZ = 0;
                    player1.getBipedRightUpperArmwear().offsetZ = 0;
                    player1.getBipedBodywear().offsetZ = 0;
                    player1.getBipedLeftUpperLegwear().offsetZ = 0;
                    player1.getBipedLeftLowerLegwear().offsetZ = 0;
                    player1.getBipedRightUpperLegwear().offsetZ = 0;
                    player1.getBipedRightLowerLegwear().offsetZ = 0;


                }
            }
            return;
        }

        float heldPercent = state / 100F;
        if (CosmeticsUtil.shouldHide(null))
            return;
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
        public int frames = 0;
        private long systemTime;
        private boolean toggled;

        public AnimationState() {
            this.systemTime = Minecraft.getSystemTime();
            this.toggled = false;
        }

        public boolean isToggled() {
            return toggled;
        }

        public void setToggled(boolean toggled) {
            this.toggled = toggled;
        }

        private void update(long systemTime) {
            while (this.systemTime < systemTime + (1000 / 60)) {
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

        public boolean shouldReset() {
            return this.frames == 1;
        }

        public int getFrames() {
            return frames;
        }
    }
}
