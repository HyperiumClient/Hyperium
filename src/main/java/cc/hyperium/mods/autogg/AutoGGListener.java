package cc.hyperium.mods.autogg;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

/**
 * Main listener for AutoGG
 */
public class AutoGGListener {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final AutoGG mod;

    public AutoGGListener(AutoGG mod) {
        this.mod = mod;
    }

    @InvokeEvent
    public void onChat(final ChatEvent event) {
        // Make sure the mod is enabled
        if (
//            !this.mod.isHypixel() ||
                !this.mod.getConfig().isToggled() || this.mod.isRunning() || this.mod.getTriggers().isEmpty()) {
            return;
        }

        // Double parse to remove hypixel formatting codes
        String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());

        if (this.mod.getTriggers().stream().anyMatch(unformattedMessage::contains) && unformattedMessage.startsWith(" ")) {
            this.mod.setRunning(true);

            // The GGThread in an anonymous class
            Multithreading.POOL.submit(() -> {
                try {
                    Thread.sleep(((AutoGG) Hyperium.INSTANCE.getModIntegration().getAutoGG()).getConfig().getDelay() * 1000);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/achat gg");
                    Thread.sleep(2000L);

                    // We are referring to it from a different thread, thus we need to do this
                    ((AutoGG) Hyperium.INSTANCE.getModIntegration().getAutoGG()).setRunning(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
