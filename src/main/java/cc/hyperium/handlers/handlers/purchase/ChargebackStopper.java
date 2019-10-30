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

package cc.hyperium.handlers.handlers.purchase;

import cc.hyperium.netty.utils.Utils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.UUIDUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import java.util.UUID;

public class ChargebackStopper {

    public ChargebackStopper() {
        Multithreading.runAsync(() -> {
                UUID clientUUID = UUIDUtil.getClientUUID();
                if (clientUUID != null) {
                    JsonHolder holder = Utils.get("https://api.hyperium.cc/banned");
                    JsonArray bans = holder.optJSONArray("bans");

                    for (JsonElement ban : bans) {
                        JsonHolder holder1 = new JsonHolder(ban.getAsJsonObject());
                        if (holder1.optString("uuid").equalsIgnoreCase(clientUUID.toString())) {
                            //Banned
                            Minecraft.getMinecraft().crashed(new CrashReport(
                                "You are currently blocked from using Hyperium for a chargeback." +
                                    " Please contact Hyperium Administrators to resolve this.", new Throwable()));
                        }
                    }
                }
            }
        );
    }
}
