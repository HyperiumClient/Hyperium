package com.hcc.mods.sk1ercommon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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
    private boolean enabled = true;
    private String apiKey;
    private JsonHolder en;
    private GenKeyCallback callback;

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


    public String getApIKey() {
        return apiKey;
    }


    public JsonObject getPlayer(String name) {
        return new JsonParser().parse(rawWithAgent("http://sk1er.club/data/" + name + "/" + getApIKey())).getAsJsonObject();
    }

    public void checkStatus() {
        Multithreading.schedule(() -> {
            en = new JsonHolder(rawWithAgent("http://sk1er.club/genkey?name=" + Minecraft.getMinecraft().getSession().getProfile().getName()
                    + "&uuid=" + Minecraft.getMinecraft().getSession().getPlayerID().replace("-", "")
                    + "&mcver=" + Minecraft.getMinecraft().getVersion()
                    + "&modver=" + version
                    + "&mod=" + modid
            ));
            if (callback != null)
                callback.call(en);
            enabled = en.optBoolean("enabled");
            apiKey = en.optString("key");
        }, 0, 5, TimeUnit.MINUTES);
    }


    public String rawWithAgent(String url) {
        url = url.replace(" ", "%20");
        System.out.println("Fetching " + url);
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (" + modid + " V" + version + ") via HCC ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return IOUtils.toString(is, Charset.forName("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return object.toString();

    }


}
