package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonManifest

import java.io.File

/**
 * Type of Strategy which should be used
 * to load an Addon.
 *
 * @author Kevin Brewster
 * @since 1.0
 */
abstract class AddonLoaderStrategy {

    /**
     * Override this method in the AddonLoader
     *
     * @param file file to load
     * @throws Exception when exception occurs
     */
    @Throws(Exception::class)
    abstract fun load(file: File?): AddonManifest?


}
