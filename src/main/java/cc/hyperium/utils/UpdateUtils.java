package cc.hyperium.utils;

import cc.hyperium.Metadata;

import static cc.hyperium.installer.InstallerFrame.get;

/**
 * @author Cubxity
 */

public class UpdateUtils {
    public JsonHolder vJson;

    public boolean isUpdated() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        JsonHolder json;
        try {
            json = new JsonHolder(get(versions_url));
            this.vJson = json;
            return Metadata.getVersionID() >= json.optInt("latest-id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
