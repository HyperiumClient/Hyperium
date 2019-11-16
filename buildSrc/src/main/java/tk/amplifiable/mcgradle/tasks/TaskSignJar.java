package tk.amplifiable.mcgradle.tasks;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import groovy.lang.Closure;
import groovy.util.MapEntry;
import org.gradle.api.DefaultTask;
import org.gradle.api.NonNullApi;
import org.gradle.api.Project;
import org.gradle.api.file.FileTreeElement;
import org.gradle.api.file.FileVisitDetails;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@NonNullApi
public class TaskSignJar extends DefaultTask implements PatternFilterable {
    private PatternSet patternSet = new PatternSet();
    private String alias;
    private String storePass;
    private String keyPass;
    private String keyStore;
    private Object inputFile;
    private Object outputFile;

    @TaskAction
    private void doTask() throws IOException {
        Map<String, Map.Entry<byte[], Long>> ignored = Maps.newHashMap();
        File input = getInputFile();
        File toSign = new File(getTemporaryDir(), input.getName() + ".unsigned.tmp");
        File signed = new File(getTemporaryDir(), input.getName() + ".signed.tmp");
        File output = getOutputFile();
        processInputJar(input, toSign, ignored);
        Map<String, Object> map = Maps.newHashMap();
        map.put("alias", getAlias());
        map.put("storePass",  getStorePass());
        map.put("jar", toSign.getAbsolutePath());
        map.put("signedJar", signed.getAbsolutePath());
        if (!Strings.isNullOrEmpty(getKeyPass()))
            map.put("keypass", getKeyPass());
        if (!Strings.isNullOrEmpty(getKeyStore()))
            map.put("keyStore", getKeyStore());
        getProject().getAnt().invokeMethod("signjar", map);
        writeOutputJar(signed, output, ignored);
    }

    private void processInputJar(File inputJar, File toSign, Map<String, Map.Entry<byte[], Long>> unsigned) throws IOException {
        Spec<FileTreeElement> spec = patternSet.getAsSpec();
        toSign.getParentFile().mkdirs();
        JarOutputStream outs = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(toSign)));
        getProject().zipTree(inputJar).visit(new FileVisitor() {
            @Override
            public void visitDir(FileVisitDetails dirDetails) {
                try {
                    String path = dirDetails.getPath();
                    ZipEntry entry = new ZipEntry(path.endsWith("/") ? path : path + "/");
                    outs.putNextEntry(entry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitFile(FileVisitDetails fileDetails) {
                try {
                    if (spec.isSatisfiedBy(fileDetails)) {
                        ZipEntry entry = new ZipEntry(fileDetails.getPath());
                        entry.setTime(fileDetails.getLastModified());
                        outs.putNextEntry(entry);
                        fileDetails.copyTo(outs);
                        outs.closeEntry();
                    } else {
                        try (InputStream stream = fileDetails.open()) {
                            //noinspection unchecked
                            unsigned.put(fileDetails.getPath(), new MapEntry(ByteStreams.toByteArray(stream), fileDetails.getLastModified()));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        outs.close();
    }

    private void writeOutputJar(File signedJar, File outputJar, Map<String, Map.Entry<byte[], Long>> unsigned) throws IOException {
        outputJar.getParentFile().mkdirs();
        try (JarOutputStream outs = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputJar)))) {
            try (ZipFile base = new ZipFile(signedJar)) {
                for (ZipEntry entry : Collections.list(base.entries())) {
                    if (entry.isDirectory()) {
                        outs.putNextEntry(entry);
                    } else {
                        ZipEntry n = new ZipEntry(entry.getName());
                        n.setTime(entry.getTime());
                        outs.putNextEntry(n);
                        ByteStreams.copy(base.getInputStream(entry), outs);
                        outs.closeEntry();
                    }
                }
            }
            for (Map.Entry<String, Map.Entry<byte[], Long>> entry : unsigned.entrySet()) {
                ZipEntry n = new ZipEntry(entry.getKey());
                n.setTime(entry.getValue().getValue());
                outs.putNextEntry(n);
                outs.write(entry.getValue().getKey());
                outs.closeEntry();
            }
        }
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setStorePass(String storePass) {
        this.storePass = storePass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setInputFile(Object inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(Object outputFile) {
        this.outputFile = outputFile;
    }

    private Project getProjects() {
        return getProject().getRootProject().getChildProjects().get("generated");
    }

    @InputFile
    public File getInputFile() {
        return ((Jar) getProjects().getTasks().getByName("jar")).getArchiveFile().get().getAsFile();
    }

    @OutputFile
    public File getOutputFile() {
        return ((Jar) getProjects().getTasks().getByName("jar")).getArchiveFile().get().getAsFile();
    }

    @Input
    public String getAlias() {
        return alias;
    }

    @Input
    public String getStorePass() {
        return storePass;
    }

    @Input
    public String getKeyPass() {
        return keyPass;
    }

    @Input
    public String getKeyStore() {
        return keyStore;
    }

    @Override
    public Set<String> getIncludes() {
        return patternSet.getIncludes();
    }

    @Override
    public Set<String> getExcludes() {
        return patternSet.getExcludes();
    }

    @Override
    public PatternFilterable setIncludes(Iterable<String> includes) {
        return patternSet.setIncludes(includes);
    }

    @Override
    public PatternFilterable setExcludes(Iterable<String> excludes) {
        return patternSet.setExcludes(excludes);
    }

    @Override
    public PatternFilterable include(String... includes) {
        return patternSet.include(includes);
    }

    @Override
    public PatternFilterable include(Iterable<String> includes) {
        return patternSet.include(includes);
    }

    @Override
    public PatternFilterable include(Spec<FileTreeElement> includeSpec) {
        return patternSet.include(includeSpec);
    }

    @Override
    public PatternFilterable include(Closure includeSpec) {
        return patternSet.include(includeSpec);
    }

    @Override
    public PatternFilterable exclude(String... excludes) {
        return patternSet.exclude(excludes);
    }

    @Override
    public PatternFilterable exclude(Iterable<String> excludes) {
        return patternSet.exclude(excludes);
    }

    @Override
    public PatternFilterable exclude(Spec<FileTreeElement> excludeSpec) {
        return patternSet.exclude(excludeSpec);
    }

    @Override
    public PatternFilterable exclude(Closure excludeSpec) {
        return patternSet.exclude(excludeSpec);
    }
}
