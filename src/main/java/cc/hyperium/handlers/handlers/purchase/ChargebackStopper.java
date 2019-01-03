package cc.hyperium.handlers.handlers.purchase;

import cc.hyperium.netty.utils.Utils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.Multithreading;
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
                            Minecraft.getMinecraft().crashed(new CrashReport("You are current blocked from using Hyperium for a chargeback. Please contact Hyperium Administrators to resolve this.", new Throwable()));
                        }
                    }
                }
            }
        );
    }
}
