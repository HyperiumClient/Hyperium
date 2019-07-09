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
import cc.hyperium.internal.addons.misc.AddonManifestParser
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset

/**
 * Used to load the addon if the addon is
 * being made inside of the dev environment
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class WorkspaceAddonLoader : AddonLoaderStrategy() {

    /**
     * Does not need to be added to the classloader as
     * it gets compiled from the dev environment
     */
    @Throws(Exception::class)
    override fun load(file: File?): AddonManifest? {
        val resource = javaClass.classLoader.getResource("addon.json") ?: return null // not in workspace
        if (javaClass.classLoader.getResource("pack.mcmeta") != null) {
            AddonBootstrap.addonResourcePacks.add(file)
        }

        val lines = IOUtils.toString(resource.openStream(), Charset.defaultCharset())

        val parser = AddonManifestParser(lines)

        return parser.getAddonManifest()
    }
}
