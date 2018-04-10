package cc.hyperium.internal.addons.strategy;

import cc.hyperium.internal.addons.AddonManifest;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;

/**
 * Type of Strategy which should be used
 * to load an Addon.
 *
 * @author Kevin Brewster
 * @since 1.0
 */
public abstract class AddonLoaderStrategy {

    /**
     * Classloader
     */
    protected static LaunchClassLoader classloader = Launch.classLoader;

    /**
     * Override this method in the AddonLoader
     *
     * @param file file to load
     * @throws Exception when exception occurs
     */
    public abstract AddonManifest load(File file) throws Exception;

}
