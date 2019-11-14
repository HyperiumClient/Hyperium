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

package cc.hyperium.mods.sk1ercommon;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mitchell Katz on 6/8/2017.
 */
public class Sk1erMod {
    /*
        Sk1erMod 5.0
        Dabbing intensifies
     */
    private static Sk1erMod instance;
    private String modid;
    private String version;
    private String apiKey = "";
    private JsonHolder en = new JsonHolder();
    private GenKeyCallback callback = object -> {
    };

    public Sk1erMod(String modid, String version) {
        this.modid = modid;
        this.version = version;
        instance = this;
    }

    public Sk1erMod(String modid, String version, GenKeyCallback callback) {
        this(modid, version);
        this.callback = callback;
    }

    public static Sk1erMod getInstance() {
        return instance;
    }

    public JsonHolder getResponse() {
        return en;
    }

    public boolean isEnabled() {
        return true;
    }

    public JsonObject getPlayer(String name) {
        return new JsonParser().parse(rawWithAgent("https://sk1er.club/data/" + name + "/" + apiKey)).getAsJsonObject();
    }

    public void checkStatus() {
        if (Minecraft.getMinecraft().gameSettings.snooperEnabled) {
            Multithreading.schedule(() -> {
                en = new JsonHolder(rawWithAgent("https://sk1er.club/genkey?name=" + Minecraft.getMinecraft().getSession().getProfile().getName()
                    + "&uuid=" + Minecraft.getMinecraft().getSession().getPlayerID().replace("-", "")
                    + "&mcver=" + Minecraft.getMinecraft().getVersion()
                    + "&modver=" + version
                    + "&mod=" + modid
                ));
                if (callback != null)
                    callback.call(en);
                en.optBoolean("enabled");
                apiKey = en.optString("key");
            }, 0, 5, TimeUnit.MINUTES);
        }
    }

    public String rawWithAgent(String url) {
        Hyperium.LOGGER.info("[Sk1erMod] Fetching " + url);
        if (!Hyperium.INSTANCE.isAcceptedTos()) {
            return new JsonHolder().put("success", false).put("cause", "TOS_NOT_ACCEPTED").toString();
        }

        url = url.replace(" ", "%20");

        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (" + modid + " V" + version + ") via Hyperium ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }

        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return object.toString();
    }
}
