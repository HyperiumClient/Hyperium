/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.DisplayTable;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import net.hypixel.api.GameType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BlitzStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "SG-64";
    }

    @Override
    public String getName() {
        return GameType.SURVIVAL_GAMES.getName();
    }

    @Override
    public GameType getGameType() {
        return GameType.SURVIVAL_GAMES;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        JsonHolder stats = player.getStats(GameType.SURVIVAL_GAMES);
        items.add(new DisplayLine(bold("Coins: ", stats.optInt("coins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Kills: ", stats.optInt("kills")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Wins: ", stats.optInt("wins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(stats.optInt("kills"), stats.optInt("deaths"))), Color.WHITE.getRGB()));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> preview = getPreview(player);
        preview.add(new DisplayLine(""));
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{"Kit", "Level"});
        JsonHolder blitz = player.getStats(GameType.SURVIVAL_GAMES);
        for (String st : WebsiteUtils.blitz_kits) {
            String tmp1 = null;
            if (!st.contains("Hype")) {
                tmp1 = st.replace(" ", "").toLowerCase();
            } else {
                tmp1 = st.toLowerCase();
            }
            if (blitz.has(tmp1))
                lines.add(new String[]{st, WebsiteUtils.numeral(blitz.optInt(tmp1) + 1)});
        }
        preview.add(new DisplayTable(lines));
        return preview;
    }
}
