package cc.hyperium.mods.myposition;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ServerChatEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.TimeUnit;

public class MyPosition extends AbstractMod {

    private boolean isRan;

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Auto MyPosition", "1.0", "WaningMatrix, asbyth");
    }

    @InvokeEvent
    public void chatEvent(ServerChatEvent event) {
        String message = event.getChat().getUnformattedText();

        if (Settings.AUTO_MY_POSITION) {
            if (message.startsWith("Sending you to mini") || message.startsWith("Sending you to mega")) {
                isRan = true;
                Multithreading.schedule(this::sendMyPosCommand, (long) Settings.AUTO_MY_POSITION_DELAY, TimeUnit.SECONDS);
            }

            if (message.equalsIgnoreCase("The game starts in 5 seconds!") && !isRan) {
                isRan = true;
                Multithreading.schedule(this::sendMyPosCommand, (long) Settings.AUTO_MY_POSITION_DELAY, TimeUnit.SECONDS);
            }
        }
    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        if (isRan) Multithreading.schedule(() -> isRan = false, 15, TimeUnit.SECONDS);
    }

    private void sendMyPosCommand() {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(
            ChatColor.RED.toString() + ChatColor.BOLD + "------| Auto MyPosition |------"));
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/myposition");
    }
}
