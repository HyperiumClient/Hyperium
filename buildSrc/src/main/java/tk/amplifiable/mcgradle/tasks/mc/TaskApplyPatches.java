package tk.amplifiable.mcgradle.tasks.mc;

import com.cloudbees.diff.ContextualPatch;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.File;

public class TaskApplyPatches extends DefaultTask {

    @InputDirectory
    private File patches;

    @InputDirectory
    private File sources;

    @OutputDirectory
    private File output;

    @TaskAction
    private void applyPatches() {
        if (!patches.exists()) return;
        if (!sources.equals(output)) {
            getProject().fileTree(sources).visit(new FileVisitor() {
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
        getProject().fileTree(patches).visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {

            }

            @Override
            public void visitFile(FileVisitDetails fileDetails) {
                if (!fileDetails.getName().endsWith(".patch")) return;
                File patchFile = fileDetails.getFile();
                String fileName = patchFile.getPath();
                fileName = fileName.substring(patches.getPath().length());
                fileName = fileName.substring(0, fileName.length() - ".java.patch".length());
                fileName = fileName.replace('.', '/');
                File toPatch = new File(output, fileName + ".java");
                if (!toPatch.exists()) {
                    throw new GradleException("File " + toPatch + " doesn't exist");
                }
                ContextualPatch patch = ContextualPatch.create(patchFile, toPatch);
                try {
                    for (ContextualPatch.PatchReport report : patch.patch(false)) {
                        if (report.getStatus() != ContextualPatch.PatchStatus.Patched) {
                            throw report.getFailure();
                        }
                    }
                } catch (Throwable e) {
                    throw new GradleException("Failed to patch file " + patchFile.getName(), e);
                }
            }
        });
    }

    public void setPatches(File patches) {
        this.patches = patches;
    }

    public void setSources(File sources) {
        this.sources = sources;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    @InputDirectory
    public File getPatches() {
        return patches;
    }

    @InputDirectory
    public File getSources() {
        return sources;
    }

    @OutputDirectory
    public File getOutput() {
        return output;
    }
}
