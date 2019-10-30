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

package cc.hyperium.mixinsimp.scoreboard;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Map;

public class HyperiumScoreboard {

    private Scoreboard parent;

    public HyperiumScoreboard(Scoreboard parent) {
        this.parent = parent;
    }

    public void removeTeam(ScorePlayerTeam team, Map<String, ScorePlayerTeam> teams, Map<String, ScorePlayerTeam> teamMemberships) {
        if (team == null) return;
        if (team.getRegisteredName() != null) teams.remove(team.getRegisteredName());
        team.getMembershipCollection().forEach(teamMemberships::remove);
        parent.func_96513_c(team);
    }
}
