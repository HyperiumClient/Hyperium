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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;
import java.util.zip.Adler32;

@SuppressWarnings("UnstableApiUsage")
public class PatchManager {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final PatchManager INSTANCE = new PatchManager();
    private boolean setupComplete = false;
    private boolean disableInputCheck = false;
    private Map<String, Patch> patches = Maps.newConcurrentMap();
    private Map<String, byte[]> processedClasses = Maps.newConcurrentMap();
    private final GDiffPatcher patcher = new GDiffPatcher();

    byte[] patch(String className, byte[] classData) {
        if (processedClasses.containsKey(className)) return processedClasses.get(className);
        if (!patches.containsKey(className)) {
            return classData;
        }
        LOGGER.info("Patching {} (c1: {}, c2: {}, ch: {})", className, processedClasses.containsKey(className), patches.containsKey(className), hash(classData));
        long start = System.nanoTime();
        Patch patch = patches.get(className);
        if (!disableInputCheck) {
            long hash = hash(classData);
            if (patch.checksum != hash) {
                throw new IllegalStateException(String.format("Failed to verify patch input for %s (got %x, expected %x)", className, hash, patch.checksum));
            }
        }
        synchronized (patcher) {
            try {
                byte[] data = patcher.patch(classData, patch.patchData);
                patches.remove(className);
                processedClasses.put(className, data);
                long end = System.nanoTime();
                LOGGER.info("Patched {} in {}ms", className, ((double) end - (double) start) / 1_000_000.0);
                return data;
            } catch (IOException e) {
                LOGGER.warn("Failed to patch class {}", className, e);
                return classData;
            }
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
                        patches.put(patch.sourceName.replace('.', '/'), patch);
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
