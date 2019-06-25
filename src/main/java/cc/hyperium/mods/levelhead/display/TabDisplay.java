package cc.hyperium.mods.levelhead.display;

import cc.hyperium.mods.levelhead.Levelhead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.UUID;

public class TabDisplay extends LevelheadDisplay {

    public TabDisplay(DisplayConfig config) {
        super(DisplayPosition.TAB, config);
    }

    @Override
    public void tick() {
        for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            UUID id = info.getGameProfile().getId();

            if (id != null) {
                if (!cache.containsKey(id)) {
                    Levelhead.getInstance().fetch(id, this, false);
                }
            }
        }
    }

    @Override
    public void checkCacheSize() {
        if (cache.size() > Math.max(Levelhead.getInstance().getDisplayManager().getMasterConfig().getPurgeSize(), 150)) {
            ArrayList<UUID> safePlayers = new ArrayList<>();

            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
                UUID id = info.getGameProfile().getId();

                if (existedMoreThan5Seconds.contains(id)) {
                    safePlayers.add(id);
                }
            }

            existedMoreThan5Seconds.clear();
            existedMoreThan5Seconds.addAll(safePlayers);

            for (UUID uuid : cache.keySet()) {
                if (!safePlayers.contains(uuid)) {
                    cache.remove(uuid);
                    trueValueCache.remove(uuid);
                }
            }
        }
    }

    @Override
    public void onDelete() {
        cache.clear();
        trueValueCache.clear();
        existedMoreThan5Seconds.clear();
    }
}
