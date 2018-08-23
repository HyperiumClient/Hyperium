package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import net.hypixel.api.GameType;

public class TNTGamesStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "TNT-64";
    }

    @Override
    public String getName() {
        return GameType.TNTGAMES.getName();
    }
}
