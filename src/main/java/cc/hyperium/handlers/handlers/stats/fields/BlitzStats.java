package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import net.hypixel.api.GameType;

public class BlitzStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "SG-64";
    }

    @Override
    public String getName() {
        return GameType.SURVIVAL_GAMES.getName();
    }
}
