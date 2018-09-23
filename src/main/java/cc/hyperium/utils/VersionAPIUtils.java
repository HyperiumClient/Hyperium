package cc.hyperium.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.Charset;

public class VersionAPIUtils {
    private HttpClient httpClient;
    private String versionsUrl = "https://api.hyperium.cc/versions";

    public VersionAPIUtils(){
        httpClient = HttpClients.createDefault();
    }

    public String getDownloadLink(JsonObject json){
        JsonObject latest = json.getAsJsonObject("latest");

        String link = latest.get("url").getAsString();
        return link;
    }

    public int getVersion(JsonObject json){
        JsonObject latest = json.getAsJsonObject("latest");

        int latestId = latest.get("id").getAsInt();

        return latestId;
    }

    public JsonObject getJson(){
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(getRaw(httpClient,versionsUrl)).getAsJsonObject();

        return obj;
    }

    public String getRaw(HttpClient client, String url){
        try {
            return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";
    }
}
