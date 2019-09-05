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

package cc.hyperium.mixins.scoreboard;

import cc.hyperium.mixinsimp.scoreboard.HyperiumScoreboard;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard {

    @Shadow @Final private Map<String, ScorePlayerTeam> teams;
    @Shadow @Final private Map<String, ScorePlayerTeam> teamMemberships;

    private HyperiumScoreboard hyperiumScoreboard = new HyperiumScoreboard((Scoreboard) (Object) this);

    /**
     * @author boomboompower
     * @reason Fix NPE's
     */
    @Overwrite
    public void removeTeam(ScorePlayerTeam team) {
        hyperiumScoreboard.removeTeam(team, teams, teamMemberships);
    }
}
