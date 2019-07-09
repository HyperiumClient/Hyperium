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
        if (team == null) {
            return;
        }

        if (team.getRegisteredName() != null) {
            teams.remove(team.getRegisteredName());
        }

        for (String s : team.getMembershipCollection()) {
            teamMemberships.remove(s);
        }

        parent.func_96513_c(team);
    }
}
