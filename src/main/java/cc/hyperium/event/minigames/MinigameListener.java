package cc.hyperium.event.minigames;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinMinigameEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;

public class MinigameListener {

    private int cooldown = 3 * 20;

    String currentMinigameName = "";

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel() && Minecraft.getMinecraft().theWorld.getScoreboard() != null) {
                if (this.cooldown <= 0) {
                    this.cooldown = 3 * 20;
                    String minigameName = getScoreboardTitle();
                    Minigame[] minigames = Minigame.values();
                    for (Minigame m : Minigame.values()) {
                        if (minigameName.equalsIgnoreCase(m.scoreName) && !minigameName.equalsIgnoreCase(this.currentMinigameName)) {
                            this.currentMinigameName = minigameName;

                            EventBus.INSTANCE.post(new JoinMinigameEvent(m));
                        }
                    }
                } else {
                    this.cooldown--;
                }
            }
        }
    }

    public String getScoreboardTitle() {
        if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
            return Minecraft.getMinecraft().theWorld.getScoreboard()
                .getObjectiveInDisplaySlot(1)
                .getDisplayName().trim()
                .replace("\u00A7[0-9a-zA-Z]", "");
        }
        return "";
    }

}
