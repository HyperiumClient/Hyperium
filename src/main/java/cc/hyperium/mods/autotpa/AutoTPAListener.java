package cc.hyperium.mods.autotpa;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

public class AutoTPAListener {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final AutoTPA mod;
    public AutoTPAListener(AutoTPA mod) {
        this.mod = mod;
    }
    @InvokeEvent
    public void onChat(final ChatEvent event) {
        if(Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() && mod.getConfig().isToggled()){
            String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());
            if (unformattedMessage.contains(this.mod.getTrigger())) {
                Multithreading.POOL.submit(() -> {
                    try {
                        Thread.sleep(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig().getDelay() * 1000);
                        String player = unformattedMessage.split(this.mod.getTrigger())[0];
                        mc.thePlayer.sendChatMessage("/tpa accept "+player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
