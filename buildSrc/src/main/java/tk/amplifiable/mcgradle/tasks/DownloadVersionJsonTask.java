package tk.amplifiable.mcgradle.tasks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;
import tk.amplifiable.mcgradle.Utils;

import java.io.File;
import java.io.IOException;

public class DownloadVersionJsonTask extends AbstractDownloadTask {
    @Override
    @OutputFile
    protected File getDestination() {
        return Utils.getCacheFile(String.format(Names.VERSION_JSON, MCGradleConstants.EXTENSION.version));
    }

    @Override
    @Input
    protected String getUrl() throws IOException {
        JsonObject object = Utils.readVersionManifest();
        for (JsonElement element : object.getAsJsonArray("versions")) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.get("id").getAsString().equals(MCGradleConstants.EXTENSION.version)) {
                return obj.get("url").getAsString();
            }
        }
        throw new GradleException("Couldn't find version " + MCGradleConstants.EXTENSION.version);
    }
}
