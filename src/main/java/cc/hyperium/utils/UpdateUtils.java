package cc.hyperium.utils;

import cc.hyperium.Hyperium;
import cc.hyperium.Metadata;
import com.google.common.base.Charsets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author Cubxity
 */

public class UpdateUtils {
    private static final HttpClient client = HttpClients.createDefault();
    public static UpdateUtils INSTANCE = new UpdateUtils();
    public cc.hyperium.installer.utils.JsonHolder vJson;
    private VersionAPIUtils apiUtils = new VersionAPIUtils();

    public static cc.hyperium.installer.utils.JsonHolder get(String url) {
        try {
            return new cc.hyperium.installer.utils.JsonHolder(getRaw(url));
        } catch (Exception var2) {
            var2.printStackTrace();
            return new cc.hyperium.installer.utils.JsonHolder();
        }
    }

    public static String getRaw(String url) throws IOException {
        return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), Charsets.UTF_8);
    }

    public boolean isAbsoluteLatest() {
        JsonObject json = apiUtils.getJson();
        int version = apiUtils.getVersion(json);

        return version <= Metadata.getVersionID();
    }

    public boolean isBeta() {
        for (JsonElement element : apiUtils.getJson().get("versions").getAsJsonArray()) {
            JsonHolder holder = new JsonHolder(element.getAsJsonObject());
            if (holder.optInt("id") == Metadata.getVersionID()) {
                return holder.optBoolean("beta");
            }
        }
        return false;
    }

    public int getLatestProperVersion() {
        return apiUtils.getJson().get(Hyperium.IS_BETA ? "latest_beta" : "latest").getAsJsonObject().get("id").getAsInt();
    }
}
