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

package cc.hyperium.mods.levelhead.auth;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import me.semx11.autotip.util.LoginUtil;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class MojangAuth {

    private String accessKey;

    private boolean failed;
    private String failedMessage;

    private boolean success;
    private String hash;

    public void auth() {
        UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        JsonHolder jsonHolder = new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/auth/begin?uuid="
            + uuid + "&mod=LEVEL_HEAD&ver=" + Levelhead.VERSION));
        if (!jsonHolder.optBoolean("success")) {
            fail("Error during init: " + jsonHolder);
            return;
        }
        hash = jsonHolder.optString("hash");

        String session = Minecraft.getMinecraft().getSession().getToken();
        Hyperium.LOGGER.debug("Logging in with details: Server-Hash: {}, Session: {}, UUID: {}", hash, session, uuid);

        int statusCode = LoginUtil.joinServer(session, uuid.toString().replace("-", ""), hash);

        if (statusCode / 100 != 2) {
            fail("Error during Mojang Auth (1) " + statusCode);
            return;
        }

        JsonHolder finalResponse = new JsonHolder(Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/auth/final?hash="
            + hash + "&name=" + Minecraft.getMinecraft().getSession().getProfile().getName()));
        Hyperium.LOGGER.debug("FINAL RESPONSE: " + finalResponse);
        if (finalResponse.optBoolean("success")) {
            accessKey = finalResponse.optString("access_key");
            success = true;
            Hyperium.LOGGER.info("Successfully authenticated with Sk1er.club Levelhead");
        } else {
            fail("Error during final auth. Reason: " + finalResponse.optString("cause"));
        }
    }

    public void fail(String failedMessage) {
        this.failedMessage = failedMessage;
        failed = true;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public boolean isFailed() {
        return failed;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getHash() {
        return hash;
    }
}
