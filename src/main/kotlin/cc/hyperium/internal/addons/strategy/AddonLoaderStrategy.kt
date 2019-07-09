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
