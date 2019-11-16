package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.JsonObject;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Utils;
import tk.amplifiable.mcgradle.tasks.AbstractDownloadTask;

import java.io.IOException;

public abstract class VersionJsonDownloadTask extends AbstractDownloadTask {
    @Input
    protected String version = MCGradleConstants.EXTENSION.version;

    private JsonObject versionJson = null;

    @Override
    protected String getSha1() {
        try {
            return getDownloadObject(getVersionJson()).get("sha1").getAsString();
        } catch (IOException e) {
            throw new GradleException("Failed to fetch version JSON for version " + version, e);
        }
    }

    @Override
    @Input
    protected String getUrl() throws IOException {
        return getDownloadObject(getVersionJson()).get("url").getAsString();
    }

    protected JsonObject getVersionJson() throws IOException {
        if (versionJson == null) {
            versionJson = Utils.readVersionJson(version);
        }
        return versionJson;
    }

    protected abstract JsonObject getDownloadObject(JsonObject versionJson);

    @Input
    public String getVersion() {
        return version;
    }
}
