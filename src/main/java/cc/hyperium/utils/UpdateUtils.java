package cc.hyperium.utils;

import cc.hyperium.Metadata;
import com.google.common.base.Charsets;
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
    public static UpdateUtils INSTANCE = new UpdateUtils();
    public cc.hyperium.installer.utils.JsonHolder vJson;
    private static final HttpClient client = HttpClients.createDefault();
    private VersionAPIUtils apiUtils = new VersionAPIUtils();

    public boolean isAbsoluteLatest() {
        JsonObject json = apiUtils.getJson();
        int version = apiUtils.getVersion(json);

        if(version > Metadata.getVersionID()){
            return false;
        } else{
            return true;
        }
    }

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

}
