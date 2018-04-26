package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
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
    private ConcurrentHashMap<UUID, Boolean> rotateState = new ConcurrentHashMap<>();

    @InvokeEvent
    public void swapWorld(WorldChangeEvent event) {
        UUID id = Minecraft.getMinecraft().getSession().getProfile().getId();
        Boolean aBoolean = rotateState.get(id);
        rotateState.clear();
        if (aBoolean != null && aBoolean) {
            rotateState.put(id, true);
        }
    }

    public void state(UUID uuid, boolean state) {
        rotateState.put(uuid, state);
    }

    public void transform(EntityLivingBase bat) {
        String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
        Boolean aBoolean = rotateState.get(bat.getUniqueID());
        if (!Hyperium.INSTANCE.getHandlers().getConfigOptions().showCosmeticsEveryWhere) {
            if (!(Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName().equalsIgnoreCase("HOUSING") || Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("lobby")))
                return;
        }
        if ((aBoolean != null && aBoolean) || s != null && (s.equals("Dinnerbone") ||
                s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    public boolean getSelf() {
        return rotateState.getOrDefault(Minecraft.getMinecraft().getSession().getProfile().getId(), false);
    }
}
