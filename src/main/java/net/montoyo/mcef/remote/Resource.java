package net.montoyo.mcef.remote;

import net.montoyo.mcef.client.ClientProxy;
import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;

import java.io.File;

/**
 * A remote resource. Can be downloaded, extracted and checked.
 *
 * @author montoyo
 */
public class Resource {

    private String name;
    private boolean shouldExtract = false;

    /**
     * Constructs a remote resource from its filename and its SHA-1 checksum.
     *
     * @param name The filename of the resource.
     */
    public Resource(String name) {
        this.name = name;
    }

    /**
     * Returns the File corresponding to the specified resource.
     *
     * @param resName Name of the resource.
     * @return The File containing the location of the specified resource.
     */
    public static File getLocationOf(String resName) {
        return new File(ClientProxy.ROOT, resName);
    }

    public boolean isShouldExtract() {
        return shouldExtract;
    }

    public void setShouldExtract(boolean shouldExtract) {
        this.shouldExtract = shouldExtract;
    }

    /**
     * Checks if the file exists. Then check if its checksum is valid.
     * If the file couldn't be hashed, false will be returned.
     *
     * @return true if (and only if) the file exists and the checksum matches the {@link #} field.
     */
    public boolean exists() {
        File f = new File(ClientProxy.ROOT, name);
        if (!f.exists())
            return false;

        String hash = Util.hash(f);
        if (hash == null) {
            Log.warning("Couldn't hash file %s; assuming it doesn't exist.", f.getAbsolutePath());
            return false;
        }
        return true;
    }

    /**
     * Downloads the resource from the current mirror.
     *
     * @param ipl Progress listener. May be null.
     * @return true if the operation was successful.
     */
    public boolean download(IProgressListener ipl) {

        return Util.download(name , new File(ClientProxy.ROOT, name), true, ipl);
    }

    /**
     * If the resource is a ZIP archive, it may be extracted using this method.
     *
     * @param ipl Progress listener. May be null.
     * @return true if the operation was successful.
     */
    public boolean extract(IProgressListener ipl) {
        Util.secure(ipl).onTaskChanged("Extracting " + name);
        return Util.extract(new File(ClientProxy.ROOT, name), new File(ClientProxy.ROOT));
    }

    /**
     * Gets the filename of this resource.
     *
     * @return The filename of this resource.
     */
    public String getFileName() {
        return name;
    }

}
