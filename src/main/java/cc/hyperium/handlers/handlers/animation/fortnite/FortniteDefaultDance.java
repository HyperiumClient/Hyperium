package cc.hyperium.handlers.handlers.animation.fortnite;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.animation.AbstractPreCopyAnglesAnimationHandler;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FortniteDefaultDance extends AbstractPreCopyAnglesAnimationHandler {

    private List<AnimationFrame> frames = new ArrayList<>();
    private long duration;
    private HashMap<UUID, Long> states = new HashMap<>();

    public FortniteDefaultDance() {
        //starting frame
        generateFrames();
    }

    public void generateFrames() {
        frames.clear();
        AnimationFrame zero = new AnimationFrame(0);
        zero.name = "Zero";
        frames.add(zero);


        AnimationFrame one = new AnimationFrame(frame(10));
        one.getRightUpperArm().rotateAngleX = ((float) Math.toRadians(-85));
        one.getLeftUpperArm().rotateAngleX = ((float) Math.toRadians(-85));
        one.getLeftUpperArm().rotateAngleZ = ((float) Math.toRadians(-25));
        one.name = "One";
        frames.add(one);


        AnimationFrame two = new AnimationFrame(frame(13));
        two.getRightUpperArm().rotateAngleX = ((float) Math.toRadians(-85));
        two.getLeftUpperArm().rotateAngleX = ((float) Math.toRadians(-85));
        two.getLeftUpperArm().rotateAngleY = ((float) Math.toRadians(35));
        two.getRightUpperArm().rotateAngleY = ((float) Math.toRadians(-35));
        two.name = "Two";
        frames.add(two);


        AnimationFrame three = new AnimationFrame(frame(22));
        three.getChest().rotateAngleZ = (float) Math.toRadians(354 - 360);
        three.getHead().rotateAngleZ = (float) Math.toRadians(328 - 360);
        three.getRightUpperLeg().rotateAngleX = (float) Math.toRadians(319 - 360);
        three.getLeftUpperArm().rotateAngleX = radians(253 - 360);
        three.getLeftUpperArm().rotateAngleY = radians(352 - 360);
        three.getRightUpperArm().rotateAngleX = radians(257 - 360);
        three.getRightUpperArm().rotateAngleY = radians(5);
        three.getLeftUpperLeg().offsetY = -.075F;
        three.getLeftUpperLeg().offsetX = .075F;

        three.getRightUpperLeg().offsetY = .075F;
        three.getRightUpperLeg().offsetX = .075F;
        three.name = "Three";
        frames.add(three);

        AnimationFrame four = new AnimationFrame(frame(28));
        four.name = "four";
        four.getRightUpperArm().rotateAngleX = radians(-5);
        four.getRightUpperLeg().rotateAngleX = radians(-25);

        four.getLeftUpperLeg().rotateAngleY = radians(-12);
        four.getLeftUpperLeg().rotateAngleZ = radians(-15);
        frames.add(four);
        duration = frames.get(frames.size() - 1).getTime();
    }

    private float radians(int deg) {
        return (float) Math.toRadians(deg);
    }

    private long frame(int frame) {
        return frame * 1000 / 30;
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        states.clear();
    }

    public HashMap<UUID, Long> getStates() {
        return states;
    }

    @Override
    public float modifyState() {
        return 0;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {
        generateFrames();
        Long aLong = states.get(entity.getUniqueID());
        if (aLong == null || aLong == 0) {
            resetAnimation(player);
            get(entity.getUniqueID()).frames = 0;
            return;
        }
        long current = System.currentTimeMillis();
        long timeSinceStart = current - aLong;
        if (timeSinceStart > duration)
            states.remove(entity.getUniqueID());

        AnimationFrame prev = null;
        AnimationFrame next = null;
        for (AnimationFrame frame : frames) {
            if (prev == null || (frame.getTime() < timeSinceStart && frame.getTime() > prev.getTime())) {
                prev = frame;
            }
            if ((next == null && frame.getTime() > prev.getTime()) || (frame.getTime() > timeSinceStart && frame.getTime() < next.getTime())) {
                next = frame;
            }
        }
        if (prev == null || next == null) {
            return;
        }

        float v = (timeSinceStart - prev.getTime());
        long l = next.getTime() - prev.getTime();
        float percent = v / (float) l;

//        percent =1.0F;
//        next = frames.get(4);
//        System.out.println(prev.name+" -> " + next.name+ " " +percent);
        //Right upper arm
        adjust(player.getBipedRightUpperArmwear(), prev.getRightUpperArm().calc(percent, next.getRightUpperArm()));
        adjust(player.getBipedRightUpperArm(), prev.getRightUpperArm().calc(percent, next.getRightUpperArm()));

        //Right lower arm
        adjust(player.getBipedRightForeArm(), prev.getRightLowerArm().calc(percent, next.getRightLowerArm()));
        adjust(player.getBipedRightForeArmwear(), prev.getRightLowerArm().calc(percent, next.getRightLowerArm()));

        //Left upper arm
        adjust(player.getBipedLeftUpperArmwear(), prev.getLeftUpperArm().calc(percent, next.getLeftUpperArm()));
        adjust(player.getBipedLeftUpperArm(), prev.getLeftUpperArm().calc(percent, next.getLeftUpperArm()));

        //Left lower arm
        adjust(player.getBipedLeftForeArm(), prev.getLeftLowerArm().calc(percent, next.getLeftLowerArm()));
        adjust(player.getBipedLeftForeArmwear(), prev.getLeftLowerArm().calc(percent, next.getLeftLowerArm()));


        //Right upper Leg
        adjust(player.getBipedRightUpperLegwear(), prev.getRightUpperLeg().calc(percent, next.getRightUpperLeg()));
        adjust(player.getBipedRightUpperLeg(), prev.getRightUpperLeg().calc(percent, next.getRightUpperLeg()));

        //Right lower Leg
        adjust(player.getBipedRightLowerLeg(), prev.getRightLowerLeg().calc(percent, next.getRightLowerLeg()));
        adjust(player.getBipedRightLowerLegwear(), prev.getRightLowerLeg().calc(percent, next.getRightLowerLeg()));

        //Left upper Leg
        adjust(player.getBipedLeftUpperLegwear(), prev.getLeftUpperLeg().calc(percent, next.getLeftUpperLeg()));
        adjust(player.getBipedLeftUpperLeg(), prev.getLeftUpperLeg().calc(percent, next.getLeftUpperLeg()));

        //Left lower Leg
        adjust(player.getBipedLeftLowerLeg(), prev.getLeftLowerLeg().calc(percent, next.getLeftLowerLeg()));
        adjust(player.getBipedLeftLowerLegwear(), prev.getLeftLowerLeg().calc(percent, next.getLeftLowerLeg()));

        //Head
        adjust(player.getBipedHead(), prev.getHead().calc(percent, next.getHead()));
        adjust(player.getBipedHeadwear(), prev.getHead().calc(percent, next.getHead()));

        //Chest
        adjust(player.getBipedBody(), prev.getChest().calc(percent, next.getChest()));
        adjust(player.getBipedBodywear(), prev.getChest().calc(percent, next.getChest()));


    }

    private void adjust(ModelRenderer renderer, BodyPart part) {
        if (part.rotationPointX != 0)
            renderer.rotationPointX = part.rotationPointX;

        if (part.rotationPointY != 0)
            renderer.rotationPointY = part.rotationPointY;

        if (part.rotationPointZ != 0)
            renderer.rotationPointZ = part.rotationPointZ;

        if (part.rotateAngleX != 0)
            renderer.rotateAngleX = part.rotateAngleX;

        if (part.rotateAngleY != 0)
            renderer.rotateAngleY = part.rotateAngleY;

        if (part.rotateAngleZ != 0)
            renderer.rotateAngleZ = part.rotateAngleZ;

        if (part.offsetX != 0)
            renderer.offsetX = part.offsetX;

        if (part.offsetY != 0)
            renderer.offsetY = part.offsetY;

        if (part.offsetZ != 0)

            renderer.offsetZ = part.offsetZ;

    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        Long aLong = states.get(entity.getUniqueID());
        if (aLong == null || aLong == 0) {
            resetAnimation(player);
        }
    }
}
