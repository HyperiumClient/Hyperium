/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.scoreboard;

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
    
    /**
     * Fix NPE's
     *
     * @author boomboompower
     */
    @Overwrite
    public void removeTeam(ScorePlayerTeam team) {
        if (team == null) {
            return;
        }
        
        if (team.getRegisteredName() != null) {
            this.teams.remove(team.getRegisteredName());
        }
    
        for (String s : team.getMembershipCollection()) {
            this.teamMemberships.remove(s);
        }
    
        this.func_96513_c(team);
    }
    
    @Shadow public abstract void func_96513_c(ScorePlayerTeam playerTeam);
}