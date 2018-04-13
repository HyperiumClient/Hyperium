package cc.hyperium.mods.hgames;

import cc.hyperium.event.*;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.displayitems.hyperium.HypixelDisplay;
import cc.hyperium.mods.chromahud.displayitems.hyperium.MinigameDisplay;
import cc.hyperium.mods.hgames.minigames.Minigame;
import cc.hyperium.mods.hgames.minigames.Walls3;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import tv.twitch.chat.Chat;

public class HGames extends AbstractMod {

    private final Metadata metadata;

    private Minigame minigame;

    public HGames() {
        metadata = new Metadata(this, "Hypixel Minigames", "1.0", "Kevin");
        metadata.setDisplayName(ChatColor.GOLD + "Hypixel Minigames");

    }

    @InvokeEvent
    private void onMinigameSwitch(JoinMinigameEvent event) {
        switch(event.getMinigame()) {
            case WALLS3:
                minigame = new Walls3();
                break;
            default:
                minigame = null;
        }
    }

    @InvokeEvent
    private void onTick(TickEvent event) {
        if(minigame != null) {
            minigame.onTick();
        }
    }

    @InvokeEvent
    private void onChat(ChatEvent event) {
        if(minigame != null) {
            minigame.onChat(event.getChat());
        }
    }

    public void render(MinigameDisplay display, int starX, double startY, boolean config) {
        if(minigame != null) {
            minigame.draw(display, starX, startY, config);
        }
    }

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return metadata;
    }
}
