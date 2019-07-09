package cc.hyperium.mods.hgames.minigames;

import cc.hyperium.mods.chromahud.displayitems.hyperium.MinigameDisplay;
import net.minecraft.util.IChatComponent;

public abstract class Minigame {

    public abstract void draw(MinigameDisplay display, int starX, double startY, boolean config);

    public void onTick() {
    }

    public void onChat(IChatComponent message) {
    }

    public void onWorldChange() {
    }

}
