package net.montoyo.mcef.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import net.montoyo.mcef.client.ClientProxy;
import org.cef.OS;

/**
 * An {@link ArrayList} extended with utility functions for {@link Resource}
 *
 * @author montoyo
 */
public class ResourceList extends ArrayList<Resource> {

    /**
     * Gets a resource from its filename.
     *
     * @param file The filename of the resource.
     * @return The resource if it was found, or null.
     */
    public Resource fromFileName(String file) {
        for (Resource f : this) {
            if (f.getFileName().equalsIgnoreCase(file)) {
                return f;
            }
        }

        return null;
    }

    /**
     * Checks if every resources exists. If they exists and if their checksum are matching, they are
     * removed from the list.
     */
    public void removeExistings() {
        if (OS.isMacintosh()) {
            if (!new File(ClientProxy.ROOT, "jcef.dylib").exists()) {
                return;
            }
            File resourcesDir = new File(ClientProxy.ROOT,
                "jcef.app" + File.separator + "Contents" + File.separator + "Frameworks"
                    + File.separator + "Chromium Embedded Framework.framework" + File.separator
                    + "Resources");
            boolean hasFoundFolder = false;
            for (File file : Objects.requireNonNull(resourcesDir.listFiles())) {
                if (file.isDirectory()) {
                    hasFoundFolder = true;
                }
            }
            if (!hasFoundFolder) {
                return;
            }
        } else if (OS.isWindows() && !new File(ClientProxy.ROOT, "chrome_elf.dll").exists()) {
            return;
        }
        for (int i = 0; i < size(); i++) {
            if (get(i).exists()) {
                remove(i--);
            }
        }
    }

}
