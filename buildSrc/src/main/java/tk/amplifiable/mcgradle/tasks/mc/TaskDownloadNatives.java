package tk.amplifiable.mcgradle.tasks.mc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradle;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;
import tk.amplifiable.mcgradle.Utils;
import tk.amplifiable.mcgradle.mc.DependencyUtilities;

import java.io.File;
import java.io.IOException;

public class TaskDownloadNatives extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File versionJson = Utils.getCacheFile(String.format(Names.VERSION_JSON, version));

    @OutputDirectory
    private File output = new File(MCGradleConstants.CACHE_DIRECTORY, String.format("jars/%s/natives", version));

    @TaskAction
    private void downloadNatives() throws IOException {
        JsonObject json = Utils.readJsonObj(versionJson);
        MCGradleConstants.prepareDirectory(output);
        for (JsonElement element : json.getAsJsonArray("libraries")) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("natives") && DependencyUtilities.shouldInclude(obj)) {
                Configuration conf = getProject().getConfigurations().getByName(Names.MC_DEPENDENCIES_CONF);
                for (Dependency dep : conf.getAllDependencies()) {
                    if (DependencyUtilities.equals(obj, dep)) {
                        for (File f : conf.files(dep)) {
                            getProject().zipTree(f).visit(new FileVisitor() {
                                @Override
                                public void visitDir(FileVisitDetails dirDetails) {

                                }

                                @Override
                                public void visitFile(FileVisitDetails fileDetails) {
                                    File outputFile = new File(output, fileDetails.getPath());
                                    File parent = outputFile.getParentFile();
                                    if (parent != null) MCGradleConstants.prepareDirectory(parent);
                                    fileDetails.copyTo(outputFile);
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
