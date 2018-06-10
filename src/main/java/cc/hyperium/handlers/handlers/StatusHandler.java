package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.PurchaseApi;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StatusHandler {

    private ConcurrentHashMap<UUID, Boolean> status = new ConcurrentHashMap<>();

    @InvokeEvent
    public void world(WorldChangeEvent event) {
        status.clear();
    }

    public boolean isOnline(UUID uuid) {
        if (!status.containsKey(uuid)) {
            status.put(uuid, false);
            Multithreading.runAsync(() -> status.put(uuid, PurchaseApi.getInstance().get("https://api.hyperium.cc/online/" + uuid).optBoolean("status")));
            return false;
        }
        return status.getOrDefault(uuid, false);
    }


}
