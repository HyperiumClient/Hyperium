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

import cc.hyperium.Hyperium.LOGGER
import cc.hyperium.internal.addons.misc.AddonLoadException
import cc.hyperium.internal.addons.misc.AddonManifestParser
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader
import cc.hyperium.internal.addons.translate.InstanceTranslator
import cc.hyperium.internal.addons.translate.MixinTranslator
import cc.hyperium.internal.addons.translate.TransformerTranslator
import com.google.common.base.Stopwatch
import net.minecraft.launchwrapper.Launch
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.jar.JarFile

/**
 * Instance created on the classloader sun.misc.Launcher$AppClassLoader
 *
 * @since 1.0
 * @author Kevin Brewster
 */
object AddonBootstrap {

    /**
     * Directory where all the addonManifests are stored
     */
    private val modDirectory = File("addons")

    /**
     * Directory where all pending addons are stored
     */
    private val pendingDirectory = File("pending-addons")

    /**
     * Current (active) environment phase, set to NULL until the
     * phases have been populated
     */
    var phase: Phase = Phase.NOT_STARTED

    /**
     * Addons as resource packs to load
     */
    @JvmStatic
    val addonResourcePacks: ArrayList<File?> = arrayListOf()

    /**
     * All the filtered jars inside of the {@link #modDirectory} folder,
     */
    private var jars: ArrayList<File>

    /**
     * Method of loading all the valid addonManifests to the classloader
     */
    private val loader = DefaultAddonLoader()

    /**
     * Method of loading the addon if inside of the development environment
     */
    private val workspaceLoader = WorkspaceAddonLoader()

    /**
     * Translators which can change, add or parse an addons manifest
     * at a certain phase
     */
    internal val translators = arrayListOf(
        InstanceTranslator(),
        MixinTranslator(),
        TransformerTranslator()
    )

    /**
     * Once the {@link #init} has called it will then be populated
     */
    val addonManifests = arrayListOf<AddonManifest>()

    /**
     *
     */
    val pendingManifests = arrayListOf<AddonManifest>()

    /**
     * Loads of the files inside {@link #modDirectory} folder or
     * creates the directory if not already made and then adds
     * all the valid jar files to {@link #jars}
     */
    init {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            throw AddonLoadException("Unable to create addon directory!")
        }

        jars = modDirectory.listFiles()!!
            .filter { it.name.toLowerCase().endsWith(".jar") }
            .toCollection(arrayListOf())

    }

    /**
     * The Preinit phase of the bootstrap where all the
     * Addon Manifests are loaded into {@link #addonManifests}
     * and then sets the phase to {@link Phase#INIT}.
     *
     * This should be called before Minecraft's classes are loaded.
     * In this case we use {@link net.minecraft.launchwrapper.ITweaker}
     */
    fun init() {
        if (phase != Phase.NOT_STARTED) {
            throw AddonLoadException("Cannot initialise bootstrap twice")
        }

        phase = Phase.PREINIT
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap")
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest")

        with(addonManifests) {
            val workspaceAddon = loadWorkspaceAddon()
            //TODO: ADD DEV ENVIRONMENT CHECK
            if (workspaceAddon != null) {
                add(workspaceAddon)
            }
            addAll(loadAddons(loader))
        }

        addonManifests.forEach { manifest ->
            translators.forEach { translator -> translator.translate(manifest) }
        }

        phase = Phase.INIT
    }

    /**
     * This loads the internal addon meaning that this must be inside of a development
     * environment.
     *
     * @return returns the addon manifest
     */
    private fun loadWorkspaceAddon(): AddonManifest? {
        return try {
            loadAddon(workspaceLoader, null)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            null
        }
    }

    /**
     * Loads external jars using {@link me.kbrewster.blazeapi.internal.addons.strategy.AddonLoaderStrategy}
     * This extracts the <i>addon.json</i> from the addon and returns the parsed manifest
     *
     * @param loader Addon loader
     * @return returns a list of the addon manifests
     */
    private fun loadAddons(loader: AddonLoaderStrategy): List<AddonManifest> {
        val addons = arrayListOf<AddonManifest>()
        val pendings = if (pendingDirectory.exists()) pendingDirectory.listFiles() else arrayOf()
        try {
            if (pendingDirectory.exists())
                pendings?.forEach { pendingManifests.add(AddonManifestParser(JarFile(it)).getAddonManifest()) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        val benchmark = Stopwatch.createStarted()
        LOGGER.info("Starting to load external jars...")
        jars.forEach { jar ->
            try {
                val addon = loadAddon(loader, jar) ?: return@forEach
                addons.add(addon)
            } catch (e: Exception) {
                LOGGER.error("Could not load {}!", jar.name)
                e.printStackTrace()
            }
        }
        pendingManifests.clear()
        pendings?.forEach { jar ->
            val dest = File(modDirectory, jar.name)
            FileUtils.moveFile(jar, dest)
            try {
                val addon = loadAddon(loader, dest) ?: return@forEach
                addons.add(addon)
            } catch (e: Exception) {
                LOGGER.error("Could not load {}!", dest.name)
                e.printStackTrace()
            }
        }
        LOGGER.debug("Finished loading all jars in {}.", benchmark)
        return addons
    }

    /**
     * Loads a jar using {@link me.kbrewster.blazeapi.internal.addons.strategy.AddonLoaderStrategy}
     * This extracts the <i>addon.json</i> from the addon and returns the parsed manifest
     *
     * @param loader Addon loader strategy
     * @param addon The file which is to be loaded, can be left null if [loader] does not need it
     * @return returns the addon manifest
     */
    @Throws(Exception::class)
    private fun loadAddon(loader: AddonLoaderStrategy, addon: File?): AddonManifest? = loader.load(addon)

    /**
     * Phase the bootstrap is currently in
     */
    enum class Phase {

        /**
         * It aint started lol?
         */
        NOT_STARTED,

        /**
         * Loading classes into classloader
         * Executing AddonManifest {@link net.minecraft.launchwrapper.ITweaker} classes
         */
        PREINIT,

        /**
         * Creates instances of main classes and executes onLoad
         */
        INIT,

        /**
         * If this all phases goes successfully it will end up on the defaulted phase
         */
        DEFAULT;
    }
}
