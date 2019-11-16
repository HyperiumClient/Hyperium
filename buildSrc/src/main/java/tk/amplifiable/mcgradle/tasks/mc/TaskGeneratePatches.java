package tk.amplifiable.mcgradle.tasks.mc;

import com.cloudbees.diff.Diff;
import com.cloudbees.diff.Hunk;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TaskGeneratePatches extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputDirectory
    private File input = new File(MCGradleConstants.ROOT_PROJECT_DIR, "generated/src/main/java");

    @InputDirectory
    private File clean = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/sourceMapped");

    @OutputDirectory
    private File output = new File(MCGradleConstants.ROOT_PROJECT_DIR, "patches");

    private Set<File> created = Sets.newHashSet();

    @TaskAction
    private void generatePatches() {
        processFiles();
        removeOldFiles();
    }

    private void removeOldFiles() {
        List<File> directories = Lists.newArrayList();
        FileTree tree = getProject().fileTree(output);
        tree.visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {
                directories.add(dirDetails.getFile());
            }

            @Override
            public void visitFile(FileVisitDetails fileDetails) {
                try {
                    File file = fileDetails.getFile().getCanonicalFile();
                    if (!created.contains(file)) {
                        file.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        directories.sort((o1, o2) -> {
            int r = o1.compareTo(o2);
            return Integer.compare(0, r);
        });

        for (File f : directories) {
            if (Objects.requireNonNull(f.listFiles()).length == 0) {
                f.delete();
            }
        }
    }

    private void processFiles() {
        Set<String> commonPaths = Sets.newHashSet();
        getProject().fileTree(clean).visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {

            }

            @Override
            public void visitFile(FileVisitDetails file) {
                String relative = file.getFile().getAbsolutePath();
                relative = relative.replace('\\', '/');
                relative = relative.substring(clean.getAbsolutePath().length() + 1);
                if (new File(input, relative).exists()) {
                    commonPaths.add(relative);
                }
            }
        });
        for (String s : commonPaths) {
            try (InputStream o = new FileInputStream(new File(clean, s)); InputStream c = new FileInputStream(new File(input, s))) {
                processFile(s, o, c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processFile(String name, InputStream original, InputStream changed) throws IOException {
        File patchFile = new File(output, name.replace('\\', '/').replace('/', '.') + ".patch").getCanonicalFile();
        if (changed == null) return;
        byte[] oData = ByteStreams.toByteArray(original);
        byte[] cData = ByteStreams.toByteArray(changed);
        Diff diff = Diff.diff(new InputStreamReader(new ByteArrayInputStream(oData), StandardCharsets.UTF_8), new InputStreamReader(new ByteArrayInputStream(cData), StandardCharsets.UTF_8), false);
        if (!name.startsWith("/")) name = "/" + name;
        if (!diff.isEmpty()) {
            String diffStr = diff.toUnifiedDiff("original" + name, "changed" + name,
                    new InputStreamReader(new ByteArrayInputStream(oData), StandardCharsets.UTF_8),
                    new InputStreamReader(new ByteArrayInputStream(cData), StandardCharsets.UTF_8), 3);
            diffStr = diffStr.replace("\r\n", "\n");
            diffStr = diffStr.replace("\n" + Hunk.ENDING_NEWLINE + "\n", "\n");
            Files.touch(patchFile);
            Files.asCharSink(patchFile, StandardCharsets.UTF_8).write(diffStr);
            created.add(patchFile);
        }
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputDirectory
    public File getInput() {
        return input;
    }

    @InputDirectory
    public File getClean() {
        return clean;
    }

    @OutputDirectory
    public File getOutput() {
        return output;
    }
}
