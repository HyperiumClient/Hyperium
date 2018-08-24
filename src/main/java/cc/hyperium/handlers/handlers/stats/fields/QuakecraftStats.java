package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.DisplayTable;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.api.GameType;

/**
 * @author KodingKing
 */
public class QuakecraftStats extends AbstractHypixelStats {

  @Override
  public String getImage() {
    return "Quakecraft-64";
  }

  @Override
  public String getName() {
    return "Quakecraft";
  }

  @Override
  public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
    List<StatsDisplayItem> stats = new ArrayList<>();
    JsonHolder quakeCraft = player.getStats(GameType.QUAKECRAFT);

    stats.add(new DisplayLine(bold("Coins: ", quakeCraft.optInt("coins"))));
    stats
        .add(
            new DisplayLine(bold("Highest Killstreak: ", quakeCraft.optInt("highest_killstreak"))));
    stats.add(new DisplayLine(bold("Dash Power: ",
        Integer.valueOf(quakeCraft.has("dash_power") ? quakeCraft.optString("dash_power") : "0")
            + 1)));
    stats.add(new DisplayLine(bold("Dash Cooldown: ", Integer
        .valueOf(quakeCraft.has("dash_cooldown") ? quakeCraft.optString("dash_cooldown") : "0")
        + 1)));

    return stats;
  }

  @Override
  public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
    List<StatsDisplayItem> stats = getPreview(player);
    JsonHolder quakeCraft = player.getStats(GameType.QUAKECRAFT);

    stats.add(new DisplayLine(""));
    stats.add(new DisplayLine(""));

    List<String[]> lines = new ArrayList<>();
    lines.add(new String[]{"Mode", "Kills", "Headshots", "Wins", "Shots Fired", "Blocks Traveled",
        "Godlikes", "Killstreaks", "K/D"});

    lines.add(new String[]{
        "Overall",
        String.valueOf(quakeCraft.optInt("kills") + quakeCraft.optInt("kills_teams")),
        String.valueOf(quakeCraft.optInt("headshots") + quakeCraft.optInt("headshots_teams")),
        String.valueOf(quakeCraft.optInt("wins") + quakeCraft.optInt("wins_teams")),
        String.valueOf(
            quakeCraft.optInt("shots_fired") + quakeCraft.optInt("shots_fired_teams")),
        String.valueOf(
            quakeCraft.optInt("distance_travelled") + quakeCraft
                .optInt("distance_travelled_teams")),
        String.valueOf(quakeCraft.optInt("godlikes") + quakeCraft.optInt("godlikes_teams")),
        String.valueOf(
            quakeCraft.optInt("killstreaks") + quakeCraft.optInt("killstreaks_teams")),
        WebsiteUtils.buildRatio(quakeCraft.optInt("kills") + quakeCraft.optInt("kills_teams"),
            quakeCraft.optInt("deaths") + quakeCraft.optInt("deaths_teams"))});
    lines.add(new String[]{
        "Solo", String.valueOf(quakeCraft.optInt("kills")),
        String.valueOf(quakeCraft.optInt("headshots")),
        String.valueOf(quakeCraft.optInt("wins")),
        String.valueOf(quakeCraft.optInt("shots_fired")),
        String.valueOf(quakeCraft.optInt("distance_travelled")),
        String.valueOf(quakeCraft.optInt("godlikes")),
        String.valueOf(quakeCraft.optInt("killstreaks")),
        WebsiteUtils.buildRatio(quakeCraft.optInt("kills"),
            quakeCraft.optInt("deaths"))});
    lines.add(new String[]{
        "Teams", String.valueOf(quakeCraft.optInt("kills_teams")),
        String.valueOf(quakeCraft.optInt("headshots_teams")),
        String.valueOf(quakeCraft.optInt("wins_teams")),
        String.valueOf(quakeCraft.optInt("shots_fired_teams")),
        String.valueOf(quakeCraft.optInt("distance_travelled_teams")),
        String.valueOf(quakeCraft.optInt("godlikes_teams")),
        String.valueOf(quakeCraft.optInt("killstreaks_teams")),
        WebsiteUtils
            .buildRatio(quakeCraft.optInt("kills_teams"), quakeCraft.optInt("deaths_teams"))});

    stats.add(new DisplayTable(lines));

    return stats;
  }
}
