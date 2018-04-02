/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.launch

import cc.hyperium.FORGE
import cc.hyperium.Hyperium
import cc.hyperium.OPTIFINE
import cc.hyperium.addons.HyperiumAddonBootstrap
import cc.hyperium.addons.loader.DefaultAddonLoader
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.util.*

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 4:11 PM
 */
open class HyperiumTweaker : ITweaker {

    /**
     * Addons
     */
    val addonBootstrap  = HyperiumAddonBootstrap()
    val addonLoader = DefaultAddonLoader()

    private val args: ArrayList<String> = ArrayList()

    private val isRunningForge: Boolean =
            Launch.classLoader.transformers.any { it.javaClass.name.contains("fml") }

    private val isRunningOptifine: Boolean =
            Launch.classLoader.transformers.any { it.javaClass.name.contains("optifine") }

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
        this.args.addAll(args)
        addArgs(mapOf("gameDir" to gameDir,
                "assetsDir" to assetsDir,
                "version" to profile))
    }

    override fun getLaunchTarget() =
            "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        //classLoader.addClassLoaderExclusion("org.apache.logging.log4j.simple.")

        Hyperium.LOGGER.info("Loading Addons...")
        loadAddons()

        Hyperium.LOGGER.info("Setting up Mixins...")
        MixinBootstrap.init()

        Hyperium.LOGGER.info("Applying transformers...")
        //classLoader.registerTransformer("cc.hyperium.mods.memoryfix.ClassTransformer")

        // Excludes packages from classloader
        with(MixinEnvironment.getDefaultEnvironment()) {
            if (isRunningOptifine) {
                Mixins.addConfiguration("mixins.hyperium.json")
            } else {
                Mixins.addConfiguration("mixins.hyperiumvanilla.json")
            }
            this.obfuscationContext = when {
                isRunningForge -> {
                    FORGE = true
                    "searge" // Switch's to forge searge mappings
                }
                isRunningOptifine -> {
                    OPTIFINE = true
                    "notch" // Switch's to notch mappings
                }
                else -> "notch" // Switch's to notch mappings
            }
            Hyperium.LOGGER.info("Forge {}!", if (FORGE) "found" else "not found")
            this.side = MixinEnvironment.Side.CLIENT
        }
    }

    fun loadAddons() {
        try {
            addonBootstrap.loadInternalAddon()
            addonBootstrap.loadAddons(addonLoader)
        } catch (e: Exception) {
            e.printStackTrace()
            Hyperium.LOGGER.error("Failed to load addon(s) from addons folder")
        }

    }

    override fun getLaunchArguments(): Array<String> =
            if (FORGE || OPTIFINE) arrayOf()
            else args.toTypedArray()

    private fun addArg(label: String, value: String?) {
        if (!this.args.contains("--$label") && value != null) {
            this.args.add("--$label")
            this.args.add(value)
        }
    }

    private fun addArg(args: String, file: File?) =
            file?.let {
                addArg(args, file.absolutePath)
            }!!

    private fun addArgs(args: Map<String, Any?>) =
            args.forEach { label, value ->
                when (value) {
                    is String? -> addArg(label, value)
                    is File? -> addArg(label, value)
                }
            }
}