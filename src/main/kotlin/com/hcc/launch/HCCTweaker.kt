/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.launch

import com.hcc.FORGE
import com.hcc.HCC
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
class HCCTweaker : ITweaker {

    private val args: ArrayList<String> = ArrayList()

    private val isRunningForge: Boolean =
            Launch.classLoader.transformers.any { it.javaClass.name.contains("fml") }

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
        this.args.addAll(args)
        addArgs(mapOf("gameDir" to gameDir,
                "assetsDir" to assetsDir,
                "version" to profile))
    }

    override fun getLaunchTarget() =
            "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        classLoader.addClassLoaderExclusion("org.apache.logging.log4j.")
        HCC.LOGGER.info("Setting up Mixins...")
        MixinBootstrap.init()
        // Excludes packages from classloader
        with(MixinEnvironment.getDefaultEnvironment()) {
            Mixins.addConfiguration("mixins.hcc.json")
            this.obfuscationContext = when {
                isRunningForge -> {
                    FORGE = true
                    "searge" // Switchs to forge searge mappings
                }
                else -> {
                    FORGE = false
                    "notch" // Switchs to notch mappings
                }
            }
            HCC.LOGGER.info("Forge {}!", if (FORGE) "found" else "not found")
            this.side = MixinEnvironment.Side.CLIENT
        }
    }

    override fun getLaunchArguments(): Array<String> =
            if (FORGE) arrayOf()
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