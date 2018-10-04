package cc.hyperium.addons.morefps.utils;

import cc.hyperium.Hyperium;
import net.minecraft.entity.Entity;

public class MoreFPSUtils {

    public static boolean isNPC(Entity entity) {
        return entity.getDisplayName().getUnformattedText().matches(".+8\\[NPC] .+");
    }

    public static boolean inSkywarsGame() {
        return Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName().equals("SKYWARS");
    }
}
