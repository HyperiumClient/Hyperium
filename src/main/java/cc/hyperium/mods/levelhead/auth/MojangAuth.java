package cc.hyperium.mods.levelhead.auth;

import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.JsonHolder;
import me.semx11.autotip.util.LoginUtil;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class MojangAuth {

    private String accessKey;

    private boolean failed = false;
    private String failedMessage = null;

    private boolean success = false;
    private String hash;

    public void auth() {
        String apiAuthLink = "https://api.sk1er.club/auth/";

        UUID uuid = Minecraft.getMinecraft().getSession().getProfile().getId();
        JsonHolder jsonHolder = new JsonHolder(Sk1erMod.getInstance().rawWithAgent(
            apiAuthLink + "begin?uuid=" + uuid + "&mod=LEVEL_HEAD&ver=" + Levelhead.VERSION));

        if (!jsonHolder.optBoolean("success")) {
            fail("Error during init: " + jsonHolder);
            return;
        }

        hash = jsonHolder.optString("hash");

        String session = Minecraft.getMinecraft().getSession().getToken();
        System.out.println("Logging in with details: Server-Hash: " + hash + " Session: " + session + " UUID=" + uuid);

        int statusCode = LoginUtil.joinServer(session, uuid.toString().replace("-", ""), hash);

        if (statusCode != 204) { // ok
            fail("Error during Mojang Auth (1) " + statusCode);
            return;
        }

        JsonHolder finalResponse = new JsonHolder(Sk1erMod.getInstance().rawWithAgent(
            apiAuthLink + "final?hash=" + hash + "&name=" + Minecraft.getMinecraft().getSession().getProfile().getName()));
        System.out.println("FINAL RESPONSE: " + finalResponse);

        if (finalResponse.optBoolean("success")) {
            accessKey = finalResponse.optString("access_key");
            success = true;
            System.out.println("Successfully authenticated with Sk1er.club Levelhead");
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
