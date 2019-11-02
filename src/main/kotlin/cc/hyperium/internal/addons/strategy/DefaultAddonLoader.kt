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

package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonBootstrap
import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonLoadException
import cc.hyperium.internal.addons.misc.AddonManifestParser
import net.minecraft.launchwrapper.Launch

import java.io.File
import java.util.jar.JarFile

/**
 * Used to load by a file into the classloader
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class DefaultAddonLoader : AddonLoaderStrategy() {

    /**
     * Loads the [file] into the classloader
     *
     * @param file addonManifests jar
     * @throws Exception when exception occurs
     */
    @Throws(Exception::class)
    override fun load(file: File?): AddonManifest? {
        if (file == null) {
            throw AddonLoadException("Could not load file; parameter issued was null.")
        }

        val jar = JarFile(file)
        if (jar.getJarEntry("pack.mcmeta") != null) {
            AddonBootstrap.addonResourcePacks.add(file)
        }

        val manifest = AddonManifestParser(jar).getAddonManifest()
        if (AddonBootstrap.pendingManifests.stream().anyMatch {
                it.name.equals(
                    manifest.name
                )
            }) {
            file.delete()
            return null
        }
        val uri = file.toURI()
        Launch.classLoader.addURL(uri.toURL())

        return manifest
    }
}
