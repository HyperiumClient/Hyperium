package cc.hyperium.utils;

import cc.hyperium.Metadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            String latest = json.optString("latest-stable");

            Matcher remote = Pattern.compile("(?<version>\\d.+\\.\\d.+) B(?<build>\\d.+)").matcher(latest);
            Matcher local = Pattern.compile("(?<version>\\d.+\\.\\d.+) - (Dev|Beta) B(?<build>\\d.+)").matcher(Metadata.getVersion());

            return Integer.parseInt(local.group("build")) < Integer.parseInt(remote.group("build"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
