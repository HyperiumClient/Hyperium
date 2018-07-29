package cc.hyperium.utils;

import cc.hyperium.Metadata;
import com.google.common.base.Charsets;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Cubxity
 */

public class UpdateUtils {
    public static UpdateUtils INSTANCE = new UpdateUtils();
    public cc.hyperium.installer.utils.JsonHolder vJson;
    private static final HttpClient client = HttpClients.createDefault();

    public boolean isSupported() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        cc.hyperium.installer.utils.JsonHolder json;
        try {
            json = get(versions_url);
            this.vJson = json;
            return Metadata.getVersionID() >= json.optInt("latest-supported");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAbsoluteLatest() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        cc.hyperium.installer.utils.JsonHolder json;
        try {
            json = InstallerUtils.get(versions_url);
            this.vJson = json;
            return Metadata.getVersionID() == new JsonHolder(json.optJSONArray("versions").get(0).getAsJsonObject()).optInt("release-id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
