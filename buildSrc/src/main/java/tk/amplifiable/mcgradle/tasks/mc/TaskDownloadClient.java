package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.JsonObject;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;

public class TaskDownloadClient extends VersionJsonDownloadTask {
    @OutputFile
    public File output = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/client.jar");

    @Override
    @OutputFile
    protected File getDestination() {
        return output;
    }

    @Input
    public String getVersion() {
        return version;
    }

    @Override
    protected JsonObject getDownloadObject(JsonObject versionJson) {
        return versionJson.getAsJsonObject("downloads").getAsJsonObject("client");
    }
}
