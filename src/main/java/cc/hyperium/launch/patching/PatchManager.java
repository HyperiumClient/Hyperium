/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.launch.patching;

import cc.hyperium.launch.deobf.DeobfAdapter;
import cc.hyperium.launch.deobf.DeobfRemapper;
import cc.hyperium.launch.patching.conflicts.*;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.nothome.delta.GDiffPatcher;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;

@SuppressWarnings("UnstableApiUsage")
public class PatchManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<String> IGNORED_OF_CHANGES = Arrays.asList(
            "bmx",
            "bbr",
            "avj"
    );
    private static final Map<String, ConflictTransformer> transformers = Maps.newHashMap();
    public static final PatchManager INSTANCE = new PatchManager();
    private boolean setupComplete = false;
    private boolean disableInputCheck = false;
    private Map<String, Patch> patches = Maps.newConcurrentMap();
    private Map<String, byte[]> processedClasses = Maps.newConcurrentMap();
    private final GDiffPatcher patcher = new GDiffPatcher();

    public byte[] patch(String className, byte[] classData, boolean cache) {
        if (processedClasses.containsKey(className)) return processedClasses.get(className);
        if (!patches.containsKey(className)) {
            return classData;
        }
        long start = System.nanoTime();
        Patch patch = patches.get(className);
        if (!disableInputCheck) {
            long hash = hash(classData);
            if (patch.checksum != hash) {
                if (IGNORED_OF_CHANGES.contains(className)) {
                    classData = getVanillaClassData(className, classData);
                } else if (transformers.containsKey(className)) {
                    System.out.println("crab");
                    ClassReader reader = new ClassReader(classData);
                    ClassNode node = new ClassNode();
                    reader.accept(new DeobfAdapter(node), ClassReader.EXPAND_FRAMES);
                    ClassWriter writer = new PatchDeobfClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                    transformers.get(className.replace('.', '/')).transform(node).accept(writer);
                    return writer.toByteArray();
                } else {
                    throw new IllegalStateException("crab - " + className);
                }
            }
        }
        synchronized (patcher) {
            try {
                byte[] data = patcher.patch(classData, patch.patchData);
                if (cache) patches.remove(className);
                if (cache) processedClasses.put(className, data);
                long end = System.nanoTime();
                LOGGER.debug("Patched {} in {}ms", className, ((double) end - (double) start) / 1_000_000.0);
                return data;
            } catch (IOException e) {
                LOGGER.warn("Failed to patch class {}", className, e);
                return classData;
            }
        }
    }

    private static void registerTransformers(ConflictTransformer... transformers) {
        for (ConflictTransformer transformer : transformers) {
            PatchManager.transformers.put(DeobfRemapper.INSTANCE.unmap(transformer.getClassName()), transformer);
        }
    }

    private byte[] getVanillaClassData(String name, byte[] d) {
        try {
            Class<?> mcClass = Class.forName("ave", false, ClassLoader.getSystemClassLoader());
            File mcJar = new File(mcClass.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (mcJar.isFile()) {
                try (JarFile file = new JarFile(mcJar)) {
                    ZipEntry e = file.getEntry(name + ".class");
                    if (e != null) {
                        return IOUtils.toByteArray(file.getInputStream(e));
                    } else {
                        return d;
                    }
                }
            } else {
                File classFile = new File(mcJar, name + ".class");
                if (!classFile.isFile()) {
                    return d;
                }
                return FileUtils.readFileToByteArray(classFile);
            }
        } catch (ClassNotFoundException | URISyntaxException e) {
            throw new IllegalStateException("Couldn't find MC jarfile", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read vanilla class file", e);
        }
    }

    public void setup(LaunchClassLoader classLoader, boolean disableInputCheck) throws IOException {
        if (setupComplete) {
            throw new IllegalStateException("Already set up!");
        }
        this.disableInputCheck = disableInputCheck;
        InputStream patchArchive = PatchManager.class.getResourceAsStream("/binpatches.lzma");
        if (patchArchive == null) {
            // probably in dev env, but check just in case
            if (classLoader.getClassBytes("net.minecraft.client.Minecraft") == null) {
                throw new IllegalStateException("Couldn't find crab files in production. Something is very wrong.");
            } else {
                LOGGER.warn("Failed to find crabs, but client is probably in dev env so we're skipping them");
            }
        } else {
            // Load all patches. There's probably some performance/memory usage balancing needed here, but at the moment it loads everything into memory
            // and once they're used they get deleted (or at least removed from map, so gc needed to actually delete them)
            LOGGER.info("Loading crab");
            long start = System.nanoTime();
            Pattern patchFilePattern = Pattern.compile("binpatch/client/.*.binpatch");
            LzmaInputStream decompressedInput = new LzmaInputStream(patchArchive, new Decoder());
            JarInputStream jis = new JarInputStream(decompressedInput); // the patches are inside an LZMA compressed JAR
            while (true) {
                try {
                    JarEntry entry = jis.getNextJarEntry();
                    if (entry == null) break;
                    if (patchFilePattern.matcher(entry.getName()).matches()) {
                        Patch patch = readPatch(jis);
                        String name = patch.sourceName.replace('.', '/');
                        patches.put(name, patch);
                    } else jis.closeEntry();
                } catch (IOException ex) {
                    LOGGER.warn("Failed to load a crab", ex);
                }
            }
            long end = System.nanoTime();
            LOGGER.info("Loaded {} crabs in {} milliseconds", patches.size(), ((double) end - (double) start) / 1_000_000.0);
        }
        setupComplete = true;
    }

    private Patch readPatch(InputStream in) throws IOException {
        ByteArrayDataInput input = ByteStreams.newDataInput(ByteStreams.toByteArray(in));
        String name = input.readUTF();
        String srcName = input.readUTF();
        String targetName = input.readUTF();
        boolean exists = input.readBoolean();
        long checksum = -1;
        if (exists) {
            checksum = input.readLong();
        }
        int length = input.readInt();
        byte[] patchData = new byte[length];
        input.readFully(patchData);
        return new Patch(name, srcName, targetName, exists, patchData, checksum);
    }

    long hash(byte[] bytes) {
        Adler32 hasher = new Adler32();
        hasher.update(bytes);
        return hasher.getValue();
    }

    private static class Patch {
        final String name;
        final String sourceName;
        final String targetName;
        final boolean targetHasFile;
        final byte[] patchData;
        final long checksum;

        Patch(String name, String sourceName, String targetName, boolean targetHasFile, byte[] patchData, long checksum) {
            this.name = name;
            this.sourceName = sourceName;
            this.targetName = targetName;
            this.targetHasFile = targetHasFile;
            this.patchData = patchData;
            this.checksum = checksum;
        }
    }

    static {
        registerTransformers(
                new GameSettingsTransformer(),
                new WorldClientTransformer(),
                new AbstractClientPlayerTransformer(),
                new GuiMainMenuTransformer(),
                new CrashReportTransformer(),
                new NoopTransformer("b$1"),
                new NoopTransformer("bdb$1"),
                new NoopTransformer("bdb$2"),
                new NoopTransformer("bdb$3"),
                new NoopTransformer("bdb$4"),
                new NoopTransformer("b$2"),
                new NoopTransformer("b$3"),
                new NoopTransformer("b$4"),
                new NoopTransformer("b$5"),
                new NoopTransformer("b$6"),
                new NoopTransformer("b$7"),
                new NoopTransformer("avh$1"),
                new NoopTransformer("avh$2"),
                new NoopTransformer("avh$a"),
                new NoopTransformer("bnm$1"),
                new NoopTransformer("bkp"),
                new NoopTransformer("bfk$1"),
                new NoopTransformer("bfk$2"),
                new NoopTransformer("bfk$3"),
                new NoopTransformer("bfk$4"),
                new NoopTransformer("ne"),
                new NoopTransformer("ne$1"),
                new NoopTransformer("bma"),
                new ThreadDownloadImageDataTransformer(),
                new EntityRendererTransformer(), // TODO: Complete this transformer
                new RenderManagerTransformer(),
                new RenderItemFrameTransformer(),
                new ModelRendererTransformer(),
                new TextureManagerTransformer(),
                new EffectRendererTransformer(),
                new FontRendererTransformer(),
                new ResourcePackRepositoryTransformer(),
                new ModelBoxTransformer(),
                new GuiVideoSettingsTransformer(),
                new RenderGlobalTransformer(),
                new TextureStateTransformer(),
                new BooleanStateTransformer(),
                new GlStateManagerTransformer(),
                // TODO: Write actual transformers for these classes
                new NoopTransformer("biv"),
                new NoopTransformer("bjl"),
                new NoopTransformer("bjl$1"),
                new NoopTransformer("bfr$1"),
                new NoopTransformer("bfr$2"),
                new NoopTransformer("bfr$a"),
                new NoopTransformer("bjh"),
                new NoopTransformer("bjh$1"),
                new NoopTransformer("bjh$2"),
                new NoopTransformer("bjh$3"),
                new NoopTransformer("bjh$4"),
                new NoopTransformer("bjh$5"),
                new NoopTransformer("bjh$6"),
                new NoopTransformer("bjh$7"),
                new NoopTransformer("bjh$8"),
                new NoopTransformer("bjh$9"),
                new NoopTransformer("bfn"),
                new NoopTransformer("avi"),
                new NoopTransformer("avo"),
                new NoopTransformer("bkn"),
                new NoopTransformer("bkn$1"),
                new NoopTransformer("bfh"),
                new NoopTransformer("bfn$1"),
                new NoopTransformer("bht"),
                new NoopTransformer("avv"),
                new NoopTransformer("avv$1"),
                new NoopTransformer("bhl"),
                new NoopTransformer("awi"),

                // dont need actual transformers
                new NoopTransformer("bfl$1"),
                new NoopTransformer("bfl$a"),
                new NoopTransformer("bfl$b"),
                new NoopTransformer("bfl$d"),
                new NoopTransformer("bfl$e"),
                new NoopTransformer("bfl$f"),
                new NoopTransformer("bfl$g"),
                new NoopTransformer("bfl$h"),
                new NoopTransformer("bfl$i"),
                new NoopTransformer("bfl$j"),
                new NoopTransformer("bfl$k"),
                new NoopTransformer("bfl$l"),
                new NoopTransformer("bfl$m"),
                new NoopTransformer("bfl$n"),
                new NoopTransformer("bfl$o"),
                new NoopTransformer("bfl$p"),
                new NoopTransformer("bfl$q")
        );
    }
}
