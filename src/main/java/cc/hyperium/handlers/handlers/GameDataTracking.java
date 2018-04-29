package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerSwitchEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitchellkatz on 3/17/18. Designed for production use on Sk1er.club
 */
public class GameDataTracking {

    //General class for keeping track of kills

    private final Map<String, Integer> playerKills = new HashMap<>();

    public int getKills(String user) {
        return playerKills.getOrDefault(user.toLowerCase(), 0);
    }

    public void incrimentKills(String user, int amount) {
        playerKills.put(user.toLowerCase(), getKills(user) + amount);
    }


    @InvokeEvent
    public void swap(ServerSwitchEvent event) {
        playerKills.clear();
    }
}
