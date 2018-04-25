package cc.hyperium.utils;

import cc.hyperium.internal.addons.AddonBootstrap;
import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class AddonWorkspaceResourcePack extends AbstractResourcePack
{
    public AddonWorkspaceResourcePack()
    {
        super(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta") != null ? new File(AddonWorkspaceResourcePack.class.getClassLoader().getResource("pack.mcmeta").getFile()).getParentFile() : null);
    }

    protected InputStream getInputStreamByName(String name) throws IOException
    {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, name)));
    }

    protected boolean hasResourceName(String name)
    {
        return (new File(this.resourcePackFile, name)).isFile();
    }

    public Set<String> getResourceDomains()
    {
        Set<String> set = Sets.<String>newHashSet();
        File file1 = new File(this.resourcePackFile, "assets/");

        if (file1.isDirectory())
        {
            for (File file2 : file1.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY))
            {
                String s = getRelativeName(file1, file2);

                if (!s.equals(s.toLowerCase()))
                {
                    this.logNameNotLowercase(s);
                }
                else
                {
                    set.add(s.substring(0, s.length() - 1));
                }
            }
        }

        return set;
    }
}
