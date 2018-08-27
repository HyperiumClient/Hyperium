package cc.hyperium.addons.autonico;

import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.utils.ChatColor;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class AutoNico extends AbstractAddon {
    private Random random;

    public AutoNico() {
        this.random = new Random();
    }

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "Auto Nico", "1.0", "Cubxity & SHARDcoder");
        metadata.setDisplayName(ChatColor.RED + "Auto Cheating");
        metadata.setOverlayClassPath("cc.hyperium.addons.autonico.overlay.AutoNicoOverlay");
        metadata.setDescription("An addon that plays a random NotNico sound every kill");

        return metadata;
    }

    @InvokeEvent
    public void onChatMessage(final ChatEvent event) {
        final String msg = event.getChat().getUnformattedText();
        if (Settings.AUTO_NICO && msg.startsWith("+") && msg.contains("Kill") && msg.contains("coins")) {
            this.randomSound();
        }
    }

    private void randomSound() {
        final int i = this.random.nextInt(3);
        if (i == 1) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-cheating"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }
        else if (i == 2) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-oof"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }
        else {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-yeet"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }
    }
}
