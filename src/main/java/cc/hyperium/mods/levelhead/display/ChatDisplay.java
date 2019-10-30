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

package cc.hyperium.mods.levelhead.display;

import cc.hyperium.mods.levelhead.Levelhead;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChatDisplay extends LevelheadDisplay {

    ChatDisplay(DisplayConfig config) {
        super(DisplayPosition.CHAT, config);
    }

    @Override
    public void tick() {
        if (Levelhead.getInstance().getLevelheadPurchaseStates().isChat()) {
            Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()
                .stream()
                .map(network -> network.getGameProfile().getId())
                .filter(Objects::nonNull)
                .filter(uuid -> !cache.containsKey(uuid))
                .forEach(uuid -> Levelhead.getInstance().fetch(uuid, this, false));
        }
    }

    @Override
    public void checkCacheSize() {
        if (cache.size() > Math.max(Levelhead.getInstance().getDisplayManager().getMasterConfig().getPurgeSize(), 150)) {
            ArrayList<UUID> safePlayers = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().map(info ->
                info.getGameProfile().getId()).filter(existedMoreThan5Seconds::contains).
                collect(Collectors.toCollection(ArrayList::new));

            existedMoreThan5Seconds.clear();
            existedMoreThan5Seconds.addAll(safePlayers);

            cache.keySet().stream().filter(uuid -> !safePlayers.contains(uuid)).forEach(uuid -> {
                cache.remove(uuid);
                trueValueCache.remove(uuid);
            });
        }
    }

    @Override
    public void onDelete() {
        cache.clear();
        trueValueCache.clear();
        existedMoreThan5Seconds.clear();
    }
}
