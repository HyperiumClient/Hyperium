package tk.amplifiable.mcgradle.tasks.mc;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.*;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;

public class TaskExtractSources extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File input = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/decompiled.jar");

    @OutputDirectory
    private File output = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/sources");

    @TaskAction
    private void extract() {
        getProject().zipTree(input).visit(new FileVisitor() {
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

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getInput() {
        return input;
    }

    @OutputDirectory
    public File getOutput() {
        return output;
    }
}
