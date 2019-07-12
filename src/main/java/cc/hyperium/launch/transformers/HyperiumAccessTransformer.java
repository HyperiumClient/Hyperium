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

package cc.hyperium.launch.transformers;

import cc.hyperium.Hyperium;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class HyperiumAccessTransformer extends AccessTransformer {

    private static Map<String, String> embedded = Maps.newHashMap(); //Needs to be primitive so that both classloaders get the same class.

    @SuppressWarnings("unchecked")
    public HyperiumAccessTransformer() throws Exception {
        super(HyperiumAccessTransformer.class);
        //We are in the new ClassLoader here, so we need to get the static field from the other ClassLoader.
        ClassLoader classLoader = this.getClass().getClassLoader().getClass().getClassLoader(); //Bit odd but it gets the class loader that loaded our current class loader yay java!
        Class<?> otherClazz = Class.forName(this.getClass().getName(), true, classLoader);
        Field otherField = otherClazz.getDeclaredField("embedded");
        otherField.setAccessible(true);
        embedded = (Map<String, String>) otherField.get(null);

        for (Map.Entry<String, String> e : embedded.entrySet()) {
            int old_count = getModifiers().size();
            processATFile(CharSource.wrap(e.getValue()));
            int added = getModifiers().size() - old_count;
            if (added > 0) {
                Hyperium.LOGGER.info("Loaded {} rules from AccessTransformer mod jar file {}\n", added, e.getKey());
            }
        }
    }

    public static void addJar(JarFile jar) throws IOException {
        Manifest manifest = jar.getManifest();
        String atList = manifest.getMainAttributes().getValue("FMLAT");
        if (atList == null) return;
        for (String at : atList.split(" ")) {
            JarEntry jarEntry = jar.getJarEntry("META-INF/" + at);
            if (jarEntry != null) {
                embedded.put(String.format("%s!META-INF/%s", jar.getName(), at),
                    new JarByteSource(jar, jarEntry).asCharSource(Charsets.UTF_8).read());
            }
        }
    }

    private static class JarByteSource extends ByteSource {
        private JarFile jar;
        private JarEntry entry;

        JarByteSource(JarFile jar, JarEntry entry) {
            this.jar = jar;
            this.entry = entry;
        }

        @Override
        public InputStream openStream() throws IOException {
            return jar.getInputStream(entry);
        }
    }
}
