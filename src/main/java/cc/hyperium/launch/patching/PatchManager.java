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
    private static final List<String> OF_CONFLICTS = Arrays.asList(
            "avh",
            "avh$1",
            "avh$a",
            "bnm",
            "bnm$1",
            "bmj",
            "avn",
            "bjh",
            "bjh$5",
            "bjh$6",
            "bjh$7",
            "bjh$8",
            "bjh$9",
            "biu",
            "biv",
            "bjl",
            "bct",
            "bcr",
            "bkn",
            "bjg",
            "bbr",
            "bkp",
            "bfn",
            "bfk",
            "bfr",
            "bfh",
            "bht",
            "bec",
            "avo",
            "avv",
            "avi",
            "avh$2"
    );
    private static final List<String> UNFIXED_CONFLICTS = Arrays.asList(
            "avh",
            "avh$1",
            "avh$a",
            "bnm",
            "bnm$1",
            "bmj",
            "avn",
            "bjh",
            "bjh$5",
            "bjh$6",
            "bjh$7",
            "bjh$8",
            "bjh$9",
            "biu",
            "biv",
            "bjl",
            "bct",
            "bcr",
            "bkn",
            "bjg",
            "bbr",
            "bkp",
            "bfn",
            "bfk",
            "bfr",
            "bfh",
            "bht",
            "bec",
            "avo",
            "avv",
            "avi",
            "avh$2"
    );
    public static final PatchManager INSTANCE = new PatchManager();
    private boolean setupComplete = false;
    private boolean disableInputCheck = false;
    private Map<String, Patch> patches = Maps.newConcurrentMap();
    private Map<String, byte[]> processedClasses = Maps.newConcurrentMap();
    private final GDiffPatcher patcher = new GDiffPatcher();

    byte[] patch(String className, byte[] classData) {
        if (processedClasses.containsKey(className)) return processedClasses.get(className);
        if (!patches.containsKey(className)) {
            return patches.isEmpty() ? classData : getVanillaClassData(className, classData);
        }
        LOGGER.debug("Patching {} (c1: {}, c2: {}, ch: {})", className, processedClasses.containsKey(className), patches.containsKey(className), hash(classData));
        long start = System.nanoTime();
        Patch patch = patches.get(className);
        if (!disableInputCheck) {
            long hash = hash(classData);
            if (patch.checksum != hash) {
                if (UNFIXED_CONFLICTS.contains(className)) {
                    LOGGER.warn("Detected OptiFine conflict with class {}, using vanilla class data", className);
                }
                classData = getVanillaClassData(className, classData);
            }
        }
        synchronized (patcher) {
            try {
                byte[] data = patcher.patch(classData, patch.patchData);
                patches.remove(className);
                processedClasses.put(className, data);
                long end = System.nanoTime();
                LOGGER.debug("Patched {} in {}ms", className, ((double) end - (double) start) / 1_000_000.0);
                return data;
            } catch (IOException e) {
                LOGGER.warn("Failed to patch class {}", className, e);
                return classData;
            }
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
                throw new IllegalStateException("Couldn't find binary patch files in production. Something is very wrong.");
            } else {
                LOGGER.warn("Failed to find binary patches, but client is probably in dev env so we're skipping them");
            }
        } else {
            // Load all patches. There's probably some performance/memory usage balancing needed here, but at the moment it loads everything into memory
            // and once they're used they get deleted (or at least removed from map, so gc needed to actually delete them)
            LOGGER.info("Loading class patches");
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
                        if (OF_CONFLICTS.contains(name) && UNFIXED_CONFLICTS.contains(name)) {
                            throw new IllegalStateException("Patch present for " + name + ", but it's incompatible with OptiFine (or not marked as incompatibility fixed).");
                        }
                        patches.put(name, patch);
                    } else jis.closeEntry();
                } catch (IOException ex) {
                    LOGGER.warn("Failed to load a patch", ex);
                }
            }
            long end = System.nanoTime();
            LOGGER.info("Loaded {} binary patches in {} milliseconds", patches.size(), ((double) end - (double) start) / 1_000_000.0);
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

    private long hash(byte[] bytes) {
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
}
