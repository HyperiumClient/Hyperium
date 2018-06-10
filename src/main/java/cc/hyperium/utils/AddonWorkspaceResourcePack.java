package cc.hyperium.utils;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class AddonWorkspaceResourcePack extends AbstractResourcePack {
    public AddonWorkspaceResourcePack() {
        super(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta") != null ? new File(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta").getFile()).getParentFile() : null);
    }

    protected InputStream getInputStreamByName(String name) throws IOException {
        return AddonWorkspaceResourcePack.class.getClassLoader().getResourceAsStream("pack.mcmeta");
    }

    protected boolean hasResourceName(String name) {
        return (new File(this.resourcePackFile, name)).isFile();
    }

    public Set<String> getResourceDomains() {
        Set<String> set = Sets.newHashSet();
        File file1 = new File(this.resourcePackFile, "assets/");

        if (file1.isDirectory()) {
            for (File file2 : file1.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
                String s = getRelativeName(file1, file2);

                if (!s.equals(s.toLowerCase())) {
                    this.logNameNotLowercase(s);
                } else {
                    set.add(s.substring(0, s.length() - 1));
                }
            }
        }

        return set;
    }
}
