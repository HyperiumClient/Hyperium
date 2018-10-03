package cc.hyperium.addons.autonico.overlay;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayButton;
import cc.hyperium.gui.main.components.OverlayToggle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class AutoNicoOverlay extends HyperiumOverlay {
    public AutoNicoOverlay() {
        this.getComponents().add(new OverlayToggle("Enable sounds", Settings.AUTO_NICO, (b) -> {
            Settings.AUTO_NICO = b;
        }, true));
        this.getComponents().add(new OverlayButton("\"Cheating in Minecraft\"", () -> {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-cheating"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }));
        this.getComponents().add(new OverlayButton("\"Oof\"", () -> {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-oof"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }));
        this.getComponents().add(new OverlayButton("\"Yeet\"", () -> {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("nico-yeet"), (float) Minecraft.getMinecraft().thePlayer.posX, (float) Minecraft.getMinecraft().thePlayer.posY, (float) Minecraft.getMinecraft().thePlayer.posZ));
        }));
    }
}
