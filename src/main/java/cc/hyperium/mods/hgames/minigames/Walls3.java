package cc.hyperium.mods.hgames.minigames;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.displayitems.hyperium.MinigameDisplay;

public class Walls3 extends Minigame {

    @Override
    public void draw(MinigameDisplay display, int starX, double startY, boolean config) {
        display.setHeight(10);
        display.setWidth(10);
        ElementRenderer.draw(starX, startY, "Testing");
    }

    @Override
    public void onTick() {

    }
}
