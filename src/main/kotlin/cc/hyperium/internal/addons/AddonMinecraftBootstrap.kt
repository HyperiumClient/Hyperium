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

package cc.hyperium.internal.addons

import cc.hyperium.Hyperium
import cc.hyperium.internal.addons.misc.AddonLoadException
import java.util.concurrent.ConcurrentHashMap

/**
 * Instance created on the classloader {@link net.minecraft.launchwrapper.LaunchClassLoader}
 *
 * @since 1.0
 * @author Kevin Brewster
 */
object AddonMinecraftBootstrap {


    @JvmStatic
    val LOADED_ADDONS = arrayListOf<IAddon>()
        @JvmName("getLoadedAddons") get
    @JvmStatic
    val ADDON_ERRORS = arrayListOf<Throwable>()
        @JvmName("getAddonLoadErrors") get

    @JvmStatic
    val MISSING_DEPENDENCIES_MAP = ConcurrentHashMap<AddonManifest, ArrayList<String>>()
        @JvmName("getMissingDependenciesMap") get

    @JvmStatic
    val DEPENDENCIES_LOOP_MAP = ConcurrentHashMap<AddonManifest, ArrayList<AddonManifest>>()
        @JvmName("getDependenciesLoopMap") get

    @JvmStatic
    val VERSION_CODE: String = "1.0"
        @JvmName("getVersionCode") get

    /**
     * The <i>init</i> phase of the bootstrap where the
     * instances are created and loaded to {@link me.kbrewster.blazeapi.BlazeAPI#LOADED_ADDONS}
     * and then sets the phase to {@link Phase#DEFAULT}.
     *
     * This should be called when <i>Minecraft</i> is starting
     * In this case we use the start of {@link net.minecraft.client.Minecraft#init}
     */
    @JvmStatic
    fun init() {
        try {
            if (AddonBootstrap.phase != AddonBootstrap.Phase.INIT) {
                throw AddonLoadException("Bootstrap is currently at Phase.${AddonBootstrap.phase} when it should be at Phase.INIT")
            }

            val toLoadMap = AddonBootstrap.addonManifests.map { it.name to it }.toMap().toMutableMap()
            val iterator = toLoadMap.iterator()

            // Loop will go on when changes are made. If no changes are made, we can
            // be sure that all addons have their requirements or won't be enabled
            var done = false
            while (!done) {
                done = true

                loadBeforeLoop@
                while (iterator.hasNext()) {
                    val manifest = iterator.next().value
                    val dependencies = manifest.dependencies

                    for (dependency in dependencies) {
                        val dependencyManifest = toLoadMap[dependency]
                        if (dependencyManifest == null) {
                            toLoadMap.remove(manifest.name)
                            Hyperium.LOGGER.error("Can't load addon ${manifest.name}. Its dependency, $dependency, isn't available.")
                            MISSING_DEPENDENCIES_MAP.computeIfAbsent(manifest) { ArrayList() }.add(dependency)
                            continue@loadBeforeLoop
                        }

                        if (dependencyManifest.dependencies.contains(manifest.name)) {
                            // The addons are depending on eachother. No way to enable them in the right order
                            iterator.remove()
                            toLoadMap.remove(manifest.name)
                            done = false
                            Hyperium.LOGGER.error("Can't load addon ${manifest.name} because it and ${dependencyManifest.name} depend on each other.")
                            DEPENDENCIES_LOOP_MAP.computeIfAbsent(manifest) { ArrayList() }.add(dependencyManifest)
                            continue@loadBeforeLoop
                        }
                    }
                }
            }

            val toLoad = toLoadMap.map { it.value }.toMutableList()

            val inEdges = toLoad.map { it to HashSet<AddonManifest>() }.toMap().toMutableMap()
            val outEdges = toLoad.map { it to HashSet<AddonManifest>() }.toMap().toMutableMap()

            toLoad.forEach { manifest ->
                manifest.dependencies.forEach {
                    val dependency = toLoadMap[it]
                    if (dependency != null) {
                        outEdges[manifest]!!.add(dependency)
                        inEdges[dependency]!!.add(manifest)
                    }
                }
            }

            val toSort = HashSet<AddonManifest>()
            toLoad.forEach {
                if (inEdges[it]?.size == 0) {
                    toSort.add(it)
                }
            }

            toLoad.clear()

            // order
            while (toSort.isNotEmpty()) {
                // remove a node n from toSort
                val n = toSort.iterator().next()
                toSort.remove(n)

                // insert n into L
                toLoad.add(n)

                // for each node m with an edge e from n to m do
                val it = outEdges[n]?.iterator()
                while (it!!.hasNext()) {
                    // remove edge e from the graph
                    val m = it.next()
                    it.remove()// Remove edge from n
                    inEdges[m]!!.remove(n)// Remove edge from m

                    // if m has no other incoming edges then insert m into toSort
                    if (inEdges[m]!!.isEmpty()) {
                        toSort.add(m)
                    }
                }
            }

            // Check to see if all edges are removed
            val cycle = toLoad.any {
                !inEdges[it]?.isEmpty()!!
            }

            if (cycle) {
                Hyperium.LOGGER.error("Cycle in the topological sort for the addons. No ordering possible")
                return
            }

            val dontLoad: ArrayList<AddonManifest> = arrayListOf()

            dontLoad.addAll(toLoad.filter {
                it.versionCode != VERSION_CODE
            }.also {
                it.forEach { addon ->
                    val output =
                        if (addon.versionCode != null) "Addon ${addon.name}'s version code (${addon.versionCode}) doesnt match our version code! $VERSION_CODE"
                        else "Addon ${addon.name}'s version code is null. Please include a \'\"versionCode\": \"$VERSION_CODE\"\"\' in your addon.json file."

                    Hyperium.LOGGER.error(output)
                }
            })

            toLoad.forEach { addon ->
                try {
                    if (addon.overlay != null) {
                        OverlayChecker.checkOverlayField(addon.overlay)
                    }
                } catch (e: Throwable) {
                    dontLoad.add(addon)
                    e.printStackTrace()
                    ADDON_ERRORS.add(e)
                }
            }

            dontLoad.forEach { addon ->
                toLoad.remove(addon)
            }

            val loaded = arrayListOf<IAddon>() // sorry Kevin but I want to put all errors in an arraylist
            toLoad.forEach { addon ->
                try {
                    val o = Class.forName(addon.mainClass).newInstance()
                    if (o is IAddon) {
                        loaded.add(o)
                    } else {
                        throw AddonLoadException("Main class isn't an instance of IAddon!")
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ADDON_ERRORS.add(e)
                }
            }

            LOADED_ADDONS.addAll(loaded)
            LOADED_ADDONS.forEach { it.onLoad() }
            AddonBootstrap.phase = AddonBootstrap.Phase.DEFAULT
        } catch (e: Exception) {
        }
    }

}
