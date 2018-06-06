package cc.hyperium.utils;

import cc.hyperium.Metadata;

import static cc.hyperium.installer.InstallerFrame.get;

/**
 * @author Cubxity
 */

public class UpdateUtils {
    public JsonHolder vJson;

    public boolean isSupported() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        JsonHolder json;
        try {
            json = new JsonHolder(get(versions_url));
            this.vJson = json;
            return Metadata.getVersionID() >= json.optInt("latest-supported");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAbsoluteLatest() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        JsonHolder json;
        try {
            json = new JsonHolder(get(versions_url));
            this.vJson = json;
            return Metadata.getVersionID() == new JsonHolder(json.optJSONArray("versions").get(0).getAsJsonObject()).optInt("release-id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
