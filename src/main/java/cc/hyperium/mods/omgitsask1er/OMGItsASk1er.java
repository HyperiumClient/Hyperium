package cc.hyperium.mods.omgitsask1er;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class OMGItsASk1er extends AbstractMod {
    String prevLobbyName = "";

    public OMGItsASk1er() {
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Autofriend Mod", "1.0", "ConorTheDev & 2PI");
    }

    @InvokeEvent
    public void onSk1erRenderEvent(RenderPlayerEvent event) {
        if (event.getEntity().getName().equalsIgnoreCase("sk1er") && Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("obby") && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() && Settings.OMG_ITS_A_SK1ER) {
            if (prevLobbyName != Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation()) {
                prevLobbyName = Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation();
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("omgitsask1er"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
                Hyperium.INSTANCE.getNotification().display(ChatColor.translateAlternateColorCodes('&', "&bSkeppy&f:"), ChatColor.translateAlternateColorCodes('&', "    &eOMG it's a Sk1er!!!1!11!"), 3F, null, null, java.awt.Color.CYAN);
            }
        }
    }
}
