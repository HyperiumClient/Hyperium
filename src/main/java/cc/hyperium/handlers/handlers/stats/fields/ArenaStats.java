package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import net.hypixel.api.GameType;

public class ArenaStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "Arena-64";
    }

    @Override
    public String getName() {
        return GameType.ARENA.getName();
    }
}
