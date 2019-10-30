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

package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.world.WorldChangeEvent;
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
