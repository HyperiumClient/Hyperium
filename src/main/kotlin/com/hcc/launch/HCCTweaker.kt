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
import java.util.ArrayList

/**
 * Contains utilities used to subscribe and invoke events
 *
 * @author Kevin
 * @since 10/02/2018 4:11 PM
 */
class HCCTweaker : ITweaker {

    private val args: ArrayList<String> = ArrayList()

    private val isRunningForge: Boolean =
            Launch.classLoader.transformers.
                    any { it.javaClass.name.contains("fml") }

    override fun acceptOptions(args: MutableList<String>, gameDir: File?, assetsDir: File?, profile: String?) {
        this.args.addAll(args)
        addArgs(mapOf("gameDir" to gameDir,
                "assetsDir" to assetsDir,
                "version" to profile))
    }

    override fun getLaunchTarget() =
            "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader?) {
        HCC.logger.info("Setting up Mixins...")
        MixinBootstrap.init()
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
            HCC.logger.info("Forge {}!", if (FORGE) "found" else "not found")
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