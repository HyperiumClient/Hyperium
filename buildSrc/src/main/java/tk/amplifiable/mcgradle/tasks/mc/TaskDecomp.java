package tk.amplifiable.mcgradle.tasks.mc;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.java.decompiler.code.CodeConstants;
import org.jetbrains.java.decompiler.main.DecompilerContext;
import org.jetbrains.java.decompiler.main.decompiler.BaseDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.*;
import org.jetbrains.java.decompiler.struct.StructMethod;
import org.jetbrains.java.decompiler.util.InterpreterUtil;
import org.jetbrains.java.decompiler.util.JADNameProvider;
import tk.amplifiable.mcgradle.MCGradleConstants;
import tk.amplifiable.mcgradle.Names;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TaskDecomp extends DefaultTask {
    @Input
    private String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File input = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/deobfuscated.jar");

    @OutputFile
    private File output = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/decompiled.jar");

    private FileCollection classpath = getProject().getConfigurations().getByName(Names.MC_DEPENDENCIES_CONF);

    @TaskAction
    private void decompile() throws IOException {
        File tempDir = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/decompiler");
        MCGradleConstants.prepareDirectory(tempDir);
        File tempJar = new File(tempDir, input.getName());
        Map<String, Object> decompileOptions = Maps.newHashMap();
        decompileOptions.put(IFernflowerPreferences.DECOMPILE_INNER, "1");
        decompileOptions.put(IFernflowerPreferences.DECOMPILE_GENERIC_SIGNATURES, "1");
        decompileOptions.put(IFernflowerPreferences.ASCII_STRING_CHARACTERS, "1");
        decompileOptions.put(IFernflowerPreferences.INCLUDE_ENTIRE_CLASSPATH, "1");
        decompileOptions.put(IFernflowerPreferences.REMOVE_SYNTHETIC, "1");
        decompileOptions.put(IFernflowerPreferences.REMOVE_BRIDGE, "1");
        decompileOptions.put(IFernflowerPreferences.LITERALS_AS_IS, "0");
        decompileOptions.put(IFernflowerPreferences.UNIT_TEST_MODE, "0");
        decompileOptions.put(IFernflowerPreferences.MAX_PROCESSING_METHOD, "0");
        decompileOptions.put(DecompilerContext.RENAMER_FACTORY, AdvancedJarRenamerFactory.class.getName());

        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(new File(tempDir, "decompiler.log").getAbsolutePath()));
        BaseDecompiler decompiler = new BaseDecompiler(new ByteCodeProvider(), new ArtifactSaver(tempDir), decompileOptions, logger);

        decompiler.addSpace(input, true);

        for (File library : classpath) {
            decompiler.addSpace(library, false);
        }

        decompiler.decompileContext();
        FileUtils.copyFile(tempJar, output);
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

    public static class AdvancedJarRenamerFactory implements IVariableNamingFactory {
        @Override
        public IVariableNameProvider createFactory(StructMethod structMethod) {
            return new AdvancedJarRenamer(structMethod);
        }
    }

    public static class AdvancedJarRenamer extends JADNameProvider {
        private StructMethod wrapper;
        private static final Pattern p = Pattern.compile("func_(\\d+)_.*");

        public AdvancedJarRenamer(StructMethod wrapper) {
            super(wrapper);
            this.wrapper = wrapper;
        }

        @Override
        public String renameAbstractParameter(String abstractParam, int index) {
            String result = abstractParam;
            if ((wrapper.getAccessFlags() & CodeConstants.ACC_ABSTRACT) != 0) {
                String methName = wrapper.getName();
                Matcher m = p.matcher(methName);
                if (m.matches()) {
                    result = String.format("p_%s_%d_", m.group(1), index);
                }
            }
            return result;
        }
    }

    public static class ByteCodeProvider implements IBytecodeProvider {
        @Override
        public byte[] getBytecode(String externalPath, String internalPath) throws IOException {
            File file = new File(externalPath);
            if (internalPath == null) {
                return InterpreterUtil.getBytes(file);
            } else {
                try (ZipFile archive = new ZipFile(file)) {
                    ZipEntry entry = archive.getEntry(internalPath);
                    if (entry == null) {
                        throw new IOException("Entry not found: " + internalPath);
                    }
                    return InterpreterUtil.getBytes(archive, entry);
                }
            }
        }
    }

    public static class ArtifactSaver implements IResultSaver {
        private final Map<String, ZipOutputStream> mapArchiveStreams = new HashMap<>();
        private final Map<String, Set<String>> mapArchiveEntries = new HashMap<>();
        private final File root;

        public ArtifactSaver(File tempDir) {
            this.root = tempDir;
        }

        private String getAbsolutePath(String path) {
            return new File(root, path).getAbsolutePath();
        }

        @Override
        public void saveFolder(String path) {
            File dir = new File(getAbsolutePath(path));
            if (!(dir.mkdirs() || dir.isDirectory())) {
                throw new RuntimeException("Cannot create directory " + dir);
            }
        }

        @Override
        public void copyFile(String source, String path, String entryName) {
            try {
                InterpreterUtil.copyFile(new File(source), new File(getAbsolutePath(path), entryName));
            } catch (IOException ex) {
                DecompilerContext.getLogger().writeMessage("Cannot copy " + source + " to " + entryName, ex);
            }
        }

        @Override
        public void saveClassFile(String path, String qualifiedName, String entryName, String content, int[] mapping) {
            File file = new File(getAbsolutePath(path), entryName);
            try {
                try (Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                    out.write(content);
                }
            } catch (IOException ex) {
                DecompilerContext.getLogger().writeMessage("Cannot write class file " + file, ex);
            }
        }

        @Override
        public void createArchive(String path, String archiveName, Manifest manifest) {
            File file = new File(getAbsolutePath(path), archiveName);
            try {
                if (!(file.createNewFile() || file.isFile())) {
                    throw new IOException("Cannot create file " + file);
                }

                FileOutputStream fileStream = new FileOutputStream(file);
                ZipOutputStream zipStream = manifest != null ? new JarOutputStream(fileStream, manifest) : new ZipOutputStream(fileStream);
                mapArchiveStreams.put(file.getPath(), zipStream);
            } catch (IOException ex) {
                DecompilerContext.getLogger().writeMessage("Cannot create archive " + file, ex);
            }
        }

        @Override
        public void saveDirEntry(String path, String archiveName, String entryName) {
            saveClassEntry(path, archiveName, null, entryName, null);
        }

        @Override
        public void copyEntry(String source, String path, String archiveName, String entryName) {
            String file = new File(getAbsolutePath(path), archiveName).getPath();

            if (!checkEntry(entryName, file)) {
                return;
            }

            try {
                try (ZipFile srcArchive = new ZipFile(new File(source))) {
                    try {
                        ZipEntry entry = srcArchive.getEntry(entryName);
                        if (entry != null) {
                            InputStream in = srcArchive.getInputStream(entry);
                            ZipOutputStream out = mapArchiveStreams.get(file);
                            out.putNextEntry(new ZipEntry(entryName));
                            InterpreterUtil.copyStream(in, out);
                            in.close();
                        }
                    } finally {
                        srcArchive.close();
                    }
                }
            } catch (IOException ex) {
                String message = "Cannot copy entry " + entryName + " from " + source + " to " + file;
                DecompilerContext.getLogger().writeMessage(message, ex);
            }
        }

        @Override
        public void saveClassEntry(String path, String archiveName, String qualifiedName, String entryName, String content) {
            String file = new File(getAbsolutePath(path), archiveName).getPath();

            if (!checkEntry(entryName, file)) {
                return;
            }

            try {
                ZipOutputStream out = mapArchiveStreams.get(file);
                out.putNextEntry(new ZipEntry(entryName));
                if (content != null) {
                    out.write(content.getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException ex) {
                String message = "Cannot write entry " + entryName + " to " + file;
                DecompilerContext.getLogger().writeMessage(message, ex);
            }
        }

        private boolean checkEntry(String entryName, String file) {
            Set<String> set = mapArchiveEntries.computeIfAbsent(file, k -> new HashSet<>());

            boolean added = set.add(entryName);
            if (!added) {
                String message = "Zip entry " + entryName + " already exists in " + file;
                DecompilerContext.getLogger().writeMessage(message, IFernflowerLogger.Severity.WARN);
            }
            return added;
        }

        @Override
        public void closeArchive(String path, String archiveName) {
            String file = new File(getAbsolutePath(path), archiveName).getPath();
            try {
                mapArchiveEntries.remove(file);
                mapArchiveStreams.remove(file).close();
            } catch (IOException ex) {
                DecompilerContext.getLogger().writeMessage("Cannot close " + file, IFernflowerLogger.Severity.WARN);
            }
        }
    }
}
