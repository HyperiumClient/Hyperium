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
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.hypixel.api.GameType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ArcadeStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "Arcade-64";
    }

    @Override
    public String getName() {
        return "Arcade";
    }

    @Override
    public GameType getGameType() {
        return GameType.ARCADE;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        items.add(new DisplayLine(bold("Coins: ", player.getStats(GameType.ARCADE).optInt("coins"))));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        List<StatsDisplayItem> items = getPreview(player);
        JsonHolder arcade = player.getStats(GameType.ARCADE);
        items.add(new DisplayLine(""));

        items.add(new DisplayLine("Galaxy Wars", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Kills: ", arcade.optInt("sw_kills"))));
        items.add(new DisplayLine(bold("Shots Fired: ", arcade.optInt("sw_shots_fired"))));
        items.add(new DisplayLine(bold("Rebel Kills: ", arcade.optInt("sw_rebel_kills"))));
        items.add(new DisplayLine(bold("Deaths: ", arcade.optInt("sw_deaths"))));

        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Farm Hunt", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Poop collected: ", arcade.optInt("poop_collected"))));
        items.add(new DisplayLine(bold("Farm Hunt Wins: ", arcade.optInt("wins_farm_hunt"))));

        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Bounty Hunters", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Wins: ", arcade.optInt("wins_oneinthequiver"))));
        items.add(new DisplayLine(bold("Deaths: ", arcade.optInt("deaths_oneinthequiver"))));
        items.add(new DisplayLine(bold("Bounty Kills: ", arcade.optInt("bounty_kills_oneinthequiver"))));
        items.add(new DisplayLine(bold("Kills: ", arcade.optInt("kills_oneinthequiver"))));

        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Blocking Dead", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Kills: ", arcade.optInt("kills_dayone"))));
        items.add(new DisplayLine(bold("Headshots: ", arcade.optInt("headshots_dayone"))));
        items.add(new DisplayLine(bold("Wins: ", arcade.optInt("wins_dayone"))));
        items.add(new DisplayLine(bold("Melee Weapon: ", arcade.optString("melee_weapon").toLowerCase().replace("_", " "))));

        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Football", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Wins: ", arcade.optInt("wins_soccer"))));
        items.add(new DisplayLine(bold("Goals: ", arcade.optInt("goals_soccer"))));
        items.add(new DisplayLine(bold("Power Kicks: ", arcade.optInt("powerkicks_soccer"))));

        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Mini Walls", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Arrows hit: ", arcade.optInt("arrows_hit_mini_walls"))));
        items.add(new DisplayLine(bold("Kills: ", arcade.optInt("kills_mini_walls"))));
        items.add(new DisplayLine(bold("Final Kills: ", arcade.optInt("final_kills_mini_walls"))));
        items.add(new DisplayLine(bold("Wins: ", arcade.optInt("wins_mini_walls"))));
        items.add(new DisplayLine(bold("Deaths: ", arcade.optInt("deaths_mini_walls"))));
        items.add(new DisplayLine(bold("Wither Damage: ", arcade.optInt("wither_damage_mini_walls"))));
        items.add(new DisplayLine(bold("Wither Kills: ", arcade.optInt("wither_kills_mini_walls"))));
        items.add(new DisplayLine(""));
        items.add(new DisplayLine("Other Games", Color.WHITE.getRGB(), 2));
        items.add(new DisplayLine(bold("Hole in the Wall Record : ", arcade.optInt("hitw_record_q"))));
        items.add(new DisplayLine(bold("Total Hole in the Walls Rounds: ", arcade.optInt("rounds_hole_in_the_wall"))));
        items.add(new DisplayLine(bold("Hypixel Says Rounds: ", arcade.optInt("rounds_simon_says"))));
        items.add(new DisplayLine(bold("Hypixel Says Wins: ", arcade.optInt("wins_simon_says"))));
        items.add(new DisplayLine(bold("Kills throwout: ", arcade.optInt("kills_throw_out"))));
        items.add(new DisplayLine(bold("Death throwout: ", arcade.optInt("deaths_throw_out"))));
        items.add(new DisplayLine(bold("Kills dragon wars: ", arcade.optInt("kills_dragonwars2"))));
        items.add(new DisplayLine(bold("Wins dragon wars: ", arcade.optInt("wins_dragonwars2"))));
        items.add(new DisplayLine(bold("Build Battle wins: ", arcade.optInt("wins_buildbattle"))));
        items.add(new DisplayLine(bold("Max Creeper Attack Wave: ", arcade.optInt("max_wave"))));
        items.add(new DisplayLine(bold("Party Games 1 wins: ", arcade.optInt("wins_party"))));
        items.add(new DisplayLine(bold("Party Games 2 wins: ", arcade.optInt("wins_party_2"))));
        items.add(new DisplayLine(bold("Party Games 3 wins: ", arcade.optInt("wins_party_3"))));

        return items;
    }
}
