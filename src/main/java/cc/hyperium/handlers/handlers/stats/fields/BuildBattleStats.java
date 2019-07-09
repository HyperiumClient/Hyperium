package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;

import java.util.ArrayList;
import java.util.List;

import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class BuildBattleStats extends AbstractHypixelStats {

    @Override
    public String getImage() {
        return "BuildBattle-64";
    }

    @Override
    public GameType getGameType() {
        return GameType.BUILD_BATTLE;
    }

    @Override
    public String getName() {
        return "Build Battle";
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        List<StatsDisplayItem> stats = new ArrayList<>();
        JsonHolder arcade = player.getStats(GameType.ARCADE);

        stats.add(new DisplayLine(bold("Build Battle wins: ", arcade.optInt("wins_buildbattle"))));

        return stats;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        return getPreview(player);
    }
}
