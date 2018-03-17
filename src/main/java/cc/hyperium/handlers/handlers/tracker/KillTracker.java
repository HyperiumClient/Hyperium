package cc.hyperium.handlers.handlers.tracker;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.SpawnpointChangeEvent;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class KillTracker {
    private int kills;
    //TORI is trash - Sk1er

    @InvokeEvent
    public void worldChange(SpawnpointChangeEvent event) {
        kills=0;
    }

    public void incKills() {
        kills++;
    }

    public int getKills() {
        return kills;
    }
}

