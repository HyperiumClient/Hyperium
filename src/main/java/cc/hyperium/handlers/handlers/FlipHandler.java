package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FlipHandler {
    private ConcurrentHashMap<UUID, Integer> rotateState = new ConcurrentHashMap<>();

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
        UUID id = Minecraft.getMinecraft().getSession().getProfile().getId();
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
        if (!Hyperium.INSTANCE.getHandlers().getConfigOptions().showCosmeticsEveryWhere) {
            if (Hyperium.INSTANCE.getHandlers().getLocationHandler().isLobbyOrHousing())
                return;
        }
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
        return rotateState.getOrDefault(Minecraft.getMinecraft().getSession().getProfile().getId(), 0);
    }
}
