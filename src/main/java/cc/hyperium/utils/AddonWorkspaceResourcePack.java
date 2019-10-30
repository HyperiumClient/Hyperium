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

package cc.hyperium.utils;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

public class AddonWorkspaceResourcePack extends AbstractResourcePack {

    /**
     * Check for if the addon has a pack.mcmeta file, which is required for any resource pack
     * If it's not null, retrieve the pack.mcmeta file, otherwise ignore it
     */
    public AddonWorkspaceResourcePack() {
        super(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta") !=
            null ? new File(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta").getFile()).getParentFile() : null);
    }

    /**
     * Get the file by the name
     *
     * @param name the name of the files inside the resource pack
     * @return the pack.mcmeta file
     * @throws IOException if it can't be found
     */
    protected InputStream getInputStreamByName(String name) throws IOException {
        return AddonWorkspaceResourcePack.class.getClassLoader().getResourceAsStream("pack.mcmeta");
    }

    /**
     * Check for the resource name
     *
     * @param name resource
     * @return resource file
     */
    protected boolean hasResourceName(String name) {
        return (new File(resourcePackFile, name)).isFile();
    }

    /**
     * Get the domain resource
     *
     * @return a string such as "assets/addonid/textures/texture.png"
     */
    public Set<String> getResourceDomains() {
        Set<String> set = Sets.newHashSet();
        File file1 = new File(resourcePackFile, "assets/");

        if (file1.isDirectory()) {
            Arrays.stream(file1.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)).map(file2 -> getRelativeName(file1, file2)).forEach(s -> {
                if (!s.equals(s.toLowerCase())) logNameNotLowercase(s);
                else set.add(s.substring(0, s.length() - 1));
            });
        }

        return set;
    }
}
