package cc.hyperium.event;

import cc.hyperium.event.minigames.Minigame;

/**
 * Invoked when player joins a minigame on Hypixel
 */
public class JoinMinigameEvent {

    private final Minigame minigame;

    public JoinMinigameEvent(Minigame minigame) {
        this.minigame = minigame;
    }

    public Minigame getMinigame() {
        return this.minigame;
    }
}
