package cc.hyperium.mods.killscreenshot;

import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ScreenShotHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class KillScreenshot extends AbstractMod {

    private final Metadata metadata;
    private int screenshotTimer = -1;

    public KillScreenshot() {
        Metadata metadata = new Metadata(this, "KillScreenshot", "1.0", "KodingKing");
        metadata.setDisplayName(ChatColor.AQUA + "Kill Screenshot");
        this.metadata = metadata;
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @InvokeEvent
    public void onChatRecieved(ChatEvent e) {
        if (!GeneralSetting.screenshotOnKillEnabled)
            return;
        if ((e.getChat().getUnformattedText().startsWith("+")) && (e.getChat().getUnformattedText().contains("Kill")) && (e.getChat().getUnformattedText().contains("coins"))) {
            screenshotTimer = 0;
        }
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        if (screenshotTimer == -1)
            return;
        int timeToScreenshot = 1;
        if (screenshotTimer < timeToScreenshot) {
            screenshotTimer++;
        }
        if (timeToScreenshot == screenshotTimer) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(Minecraft.getMinecraft().mcDataDir, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), Minecraft.getMinecraft().getFramebuffer()));
            screenshotTimer = -1;
            LogManager.getLogger().log(Level.INFO, "Kill Registered: Now taking screenshot.");
        }
    }

    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }
}
