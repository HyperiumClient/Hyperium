package cc.hyperium.handlers.handlers;

import cc.hyperium.cosmetics.CosmeticsUtil;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FlipHandler {
    private final Map<UUID, Integer> rotateState = new ConcurrentHashMap<>();

    private int tick = 0;

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        tick++;
    }

    public void resetTick() {
        if (this.tick > 10)
            this.tick = 0;
    }

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        if (id == null) {
            return;
        }
        Integer integer = rotateState.get(id);
        rotateState.clear();
        if (integer != null) {
            rotateState.put(id, integer);
        }
    }

    public void state(UUID uuid, int state) {
        rotateState.put(uuid, state);
    }

    public void transform(EntityLivingBase bat) {
        String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
        Integer state = rotateState.get(bat.getUniqueID());
        if (CosmeticsUtil.shouldHide(EnumPurchaseType.FLIP_COSMETIC))
            return;
        if ((state != null && state == 2) || s != null && (s.equals("Dinnerbone") ||
            s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            float y = bat.height + 0.1F;
            GlStateManager.translate(0.0F, y / 2, 0.0F);
            double l = System.currentTimeMillis() % (360 * 1.75) / 1.75;
            GlStateManager.rotate((float) l, .1F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -y / 2, 0.0F);
        } else if ((state != null && state == 1) || s != null && (s.equals("Dinnerbone") ||
            s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            {
                GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    public int getSelf() {
        return get(UUIDUtil.getClientUUID());
    }

    public int get(UUID uuid) {
        return rotateState.getOrDefault(uuid, 0);
    }
}
