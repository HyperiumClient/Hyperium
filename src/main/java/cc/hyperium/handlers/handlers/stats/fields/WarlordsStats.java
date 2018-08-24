package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
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
public class WarlordsStats extends AbstractHypixelStats {

  @Override
  public String getImage() {
    return "Warlords-64";
  }

  @Override
  public String getName() {
    return "Warlords";
  }

  @Override
  public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
    List<StatsDisplayItem> stats = new ArrayList<>();
    JsonHolder warlords = player.getStats(GameType.BATTLEGROUND);

    stats.add(new DisplayLine(bold("Coins: ", warlords.optInt("coins"))));
    stats.add(new DisplayLine(bold("Kills: ", warlords.optInt("kills"))));
    stats.add(new DisplayLine(bold("Assists: ", warlords.optInt("assists"))));
    stats.add(new DisplayLine(bold("Deaths: ", warlords.optInt("deaths"))));

    return stats;
  }

  @Override
  public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
    List<StatsDisplayItem> stats = getPreview(player);
    JsonHolder warlords = player.getStats(GameType.BATTLEGROUND);

    stats.add(new DisplayLine(bold("Wins: ", warlords.optInt("wins"))));
    stats.add(new DisplayLine(bold("K/D: ", WebsiteUtils
        .buildRatio(warlords.optInt("kills"), warlords.optInt("deaths")))));
    stats.add(new DisplayLine(bold("W/L: ",
        WebsiteUtils.buildRatio(warlords.optInt("wins"), warlords.optInt("losses")))));

    return stats;
  }
}
