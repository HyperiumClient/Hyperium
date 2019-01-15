package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PostCopyPlayerModelAnglesEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.renderer.model.IMixinModelPlayer;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.Multithreading;
import com.google.gson.JsonElement;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.command.CommandBase;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public abstract class AnimatedDance extends AbstractPreCopyAnglesAnimationHandler {

    public List<AnimationFrame> frames = new ArrayList<>();
    public boolean loaded;
    public long duration;
    private HashMap<UUID, Long> states = new HashMap<>();

    public AnimatedDance() {
        Multithreading.runAsync(() -> generateFrames(getData()));
    }

    public static void Frame(Scanner scanner) {
        while (true) {
            int time = 18;
            String in = scanner.nextLine();
            JsonHolder out = new JsonHolder();
            out.put("time", time);
            try {
                String[] args = in.split(" ");
                IChatComponent ichatcomponent = CommandBase.getChatComponentFromNthArg(null, args, 5);
                NBTTagCompound tagFromJson = JsonToNBT.getTagFromJson(ichatcomponent.getUnformattedText());
                NBTTagCompound pose = (NBTTagCompound) tagFromJson.getTag("Pose");
                HashMap<String, String> mappings = new HashMap<>();
                mappings.put("Body", "chest");
                mappings.put("Head", "head");
                mappings.put("LeftLeg", "leftUpperLeg");
                mappings.put("RightLeg", "rightUpperLeg");
                mappings.put("LeftArm", "leftUpperArm");
                mappings.put("RightArm", "rightUpperArm");
                for (String s : mappings.keySet()) {
                    JsonHolder holder = new JsonHolder();
                    NBTTagList tag = (NBTTagList) pose.getTag(s);
                    if (tag == null)
                        continue;
                    String[] obj = {"X", "Y", "Z"};
                    for (int i = 0; i < 3; i++) {
                        float floatAt = tag.getFloatAt(i);
                        if (floatAt != 0) {
                            if (floatAt > 180) {
                                floatAt -= 360;
                            }
                            holder.put("rotateAngle" + obj[i], ((float) Math.toRadians(floatAt)));
                        }
                    }
                    if (holder.getKeys().size() != 0) {
                        out.put(mappings.get(s), holder);
                    }
                }
            } catch (NBTException | net.minecraft.command.CommandException e) {
                e.printStackTrace();
            }
            System.out.println(out);
        }
    }

    public void generateFrames(JsonHolder data) {
        frames.clear();
        HashMap<String, Boolean> visibility = new HashMap<>();
        for (JsonElement element : data.optJSONArray("frames")) {
            JsonHolder h = new JsonHolder(element.getAsJsonObject());
            int time = h.optInt("time");
            AnimationFrame frame = new AnimationFrame(frame(time));
            frame.name = h.optInt("time") + "";
            for (String s : h.getKeys()) {
                visibility.put(s, true);
                if (!s.equalsIgnoreCase("time")) {
                    try {
                        Field declaredField1 = frame.getClass().getDeclaredField(s);
                        declaredField1.setAccessible(true);
                        BodyPart bodyPart = (BodyPart) declaredField1.get(frame);
                        JsonHolder holder1 = h.optJSONObject(s);
                        for (String s1 : holder1.getKeys()) {
                            Field declaredField = bodyPart.getClass().getDeclaredField(s1);
                            declaredField.setAccessible(true);
                            if (s1.equalsIgnoreCase("visible")) {
                                boolean visible = holder1.optBoolean("visible");
                                visibility.put(s, visible);
                                declaredField.setBoolean(bodyPart, visible);
                            } else {
                                float f = (float) holder1.optDouble(s1);
                                declaredField.setFloat(bodyPart, f);
                            }
                        }
                        bodyPart.getClass().getDeclaredField("visible").setBoolean(bodyPart, visibility.get(s));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.frames.add(frame);
        }
        loaded = true;
        if (frames.size() == 0)
            duration = 1L;
        else
            duration = frames.get(Math.max(0, frames.size() - 1)).getTime();
    }

    public float radians(int deg) {
        return (float) Math.toRadians(deg);
    }

    public long frame(int frame) {
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

    //Added so we can do legs
    @InvokeEvent
    public void onPostCopyPlayerModelAngles(PostCopyPlayerModelAnglesEvent event) {
        AbstractClientPlayer entity = event.getEntity();
        IMixinModelBiped player = event.getModel();
        modify(entity, player, false);
    }

    public abstract JsonHolder getData();

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {

        if (!loaded)
            return;
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

//        Right upper arm
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

        adjust(player.getButt(), prev.getButt().calc(percent, next.getButt()));


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

        renderer.showModel = part.visible;


    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        if (!loaded)
            return;
        Long aLong = states.get(entity.getUniqueID());
        if (aLong == null || aLong == 0) {
            resetAnimation(player);
            return;
        }
        long current = System.currentTimeMillis();
        long timeSinceStart = current - aLong;

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

//        Right upper arm
        adjust(player.getBipedRightUpperArm(), prev.getRightUpperArm().calc(percent, next.getRightUpperArm()));

        //Right lower arm
        adjust(player.getBipedRightForeArm(), prev.getRightLowerArm().calc(percent, next.getRightLowerArm()));

        //Left upper arm
        adjust(player.getBipedLeftUpperArm(), prev.getLeftUpperArm().calc(percent, next.getLeftUpperArm()));

        //Left lower arm
        adjust(player.getBipedLeftForeArm(), prev.getLeftLowerArm().calc(percent, next.getLeftLowerArm()));


        //Right upper Leg
        adjust(player.getBipedRightUpperLeg(), prev.getRightUpperLeg().calc(percent, next.getRightUpperLeg()));

        //Right lower Leg
        adjust(player.getBipedRightLowerLeg(), prev.getRightLowerLeg().calc(percent, next.getRightLowerLeg()));

        //Left upper Leg
        adjust(player.getBipedLeftUpperLeg(), prev.getLeftUpperLeg().calc(percent, next.getLeftUpperLeg()));

        //Left lower Leg
        adjust(player.getBipedLeftLowerLeg(), prev.getLeftLowerLeg().calc(percent, next.getLeftLowerLeg()));

        //Head
        adjust(player.getBipedHead(), prev.getHead().calc(percent, next.getHead()));
        adjust(player.getBipedHeadwear(), prev.getHead().calc(percent, next.getHead()));

        //Chest
        adjust(player.getBipedBody(), prev.getChest().calc(percent, next.getChest()));

    }
}
