package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;
import tk.amplifiable.mcgradle.Utils;
import tk.amplifiable.mcgradle.mc.DependencyUtilities;

import java.io.File;
import java.io.IOException;

public class TaskResolveDependencies extends DefaultTask {
    @Input
    public String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File versionJson = Utils.getCacheFile(String.format(Names.VERSION_JSON, version));

    @TaskAction
    private void resolveDeps() throws IOException {
        JsonObject json = Utils.readJsonObj(versionJson);

        for (JsonElement element : json.getAsJsonArray("libraries")) {
            JsonObject obj = element.getAsJsonObject();
            if (DependencyUtilities.shouldInclude(obj)) {
                getProject().getDependencies().add(Names.MC_DEPENDENCIES_CONF, DependencyUtilities.getDependencyString(obj));
            }
        }
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getVersionJson() {
        return versionJson;
    }
}
