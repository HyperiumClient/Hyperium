package cc.hyperium.update;

import cc.hyperium.Metadata;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.hyperium.installer.InstallerFrame.get;

/*
 * By ConorTheDev
 */

public class UpdateUtils {

    public String newBuild;
    public boolean updated = false;

    public boolean isUpdated() {
        String versions_url = "https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/versions.json";
        AtomicReference<JSONObject> version = new AtomicReference<>();
        String latest = null;

        JSONObject versionsJson;
        try {
            versionsJson = new JSONObject(get(versions_url));

            if (versionsJson.has("latest-stable")) {
                latest = versionsJson.optString("latest-stable");
            }

            //B(\d{2})

            Pattern pattern = Pattern.compile("B(\\d{2})");
            Matcher matcher = pattern.matcher(latest);

            if (matcher.find()) {
                Matcher matcher2 = pattern.matcher(Metadata.getVersion());

                if (matcher2.find()) {
                    if (matcher.group().equalsIgnoreCase(matcher2.group())) {
                        updated = true;
                        return true;
                    } else {
                        updated = false;
                        return false;
                    }
                }
                newBuild = matcher2.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return updated;
    }

}
