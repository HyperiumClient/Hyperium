package tk.amplifiable.mcgradle.tasks.mc;

import com.google.common.io.ByteStreams;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TaskSplitServerAndDeps extends DefaultTask {
    @Input
    public String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    public File input = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/server.jar");

    @OutputFile
    public File output = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/split_server.jar");

    @OutputFile
    public File outputDeps = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/split_server_deps.jar");

    @TaskAction
    public void split() throws IOException {
        try (ZipFile file = new ZipFile(input)) {
            try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(output)))) {
                try (ZipOutputStream zos2 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputDeps)))) {
                    for (ZipEntry s : Collections.list(file.entries())) {
                        if (s.isDirectory()) continue;
                        if (!(s.getName().endsWith(".class") || s.getName().endsWith(".java") || s.getName().startsWith("org/"))) {
                            ZipEntry entry1 = new ZipEntry(s.getName());
                            zos.putNextEntry(entry1);
                            zos.write(ByteStreams.toByteArray(file.getInputStream(s)));
                            continue;
                        }
                        if (s.getName().contains("/")) {
                            if (!s.getName().startsWith("net/minecraft")) {
                                ZipEntry entry1 = new ZipEntry(s.getName());
                                zos2.putNextEntry(entry1);
                                zos2.write(ByteStreams.toByteArray(file.getInputStream(s)));
                                continue;
                            }
                        }
                        ZipEntry entry1 = new ZipEntry(s.getName());
                        zos.putNextEntry(entry1);
                        zos.write(ByteStreams.toByteArray(file.getInputStream(s)));
                    }
                }
            }
        }
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getInput() {
        return input;
    }

    @OutputFile
    public File getOutput() {
        return output;
    }

    @OutputFile
    public File getOutputDeps() {
        return outputDeps;
    }
}
