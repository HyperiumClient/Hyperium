package tk.amplifiable.mcgradle.tasks.subprojects;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;

public class TaskCopySources extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputDirectory
    private File input = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/sourceMapped");

    @OutputDirectory
    private File output = new File(MCGradleConstants.ROOT_PROJECT_DIR, "generated/src/main");

    @TaskAction
    private void copySources() {
        File sources = new File(output, "java");
        File resources = new File(output, "resources");
        getProject().fileTree(input).visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {

            }

            @Override
            public void visitFile(FileVisitDetails fileDetails) {
                String name = fileDetails.getName();
                String path = fileDetails.getPath();
                if (/* path.startsWith("net/minecraftforge/fml/relauncher") || TODO: Generate jar instead of copying sources */path.startsWith("META-INF/") || name.startsWith("Log4j-") || name.endsWith(".der")) return;
                File outputDir = resources;
                if (name.endsWith(".java")) {
                    outputDir = sources;
                }
                File outputFile = new File(outputDir, path);
                File parent = outputFile.getParentFile();
                if (parent != null) MCGradleConstants.prepareDirectory(parent);
                fileDetails.copyTo(outputFile);
            }
        });
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputDirectory
    public File getInput() {
        return input;
    }

    @OutputDirectory
    public File getOutput() {
        return output;
    }
}
