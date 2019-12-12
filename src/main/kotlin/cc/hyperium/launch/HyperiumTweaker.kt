package cc.hyperium.launch

import cc.hyperium.Hyperium
import cc.hyperium.internal.addons.AddonBootstrap.init
import cc.hyperium.launch.patching.PatchManager
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.util.*

class HyperiumTweaker : ITweaker {

    private val hyperiumArguments = arrayListOf<String>()
    private val runningOptifine = Launch.classLoader.transformers.stream().anyMatch {
        it.javaClass.name.toLowerCase(Locale.ENGLISH).contains("optifine")
    }

    override fun acceptOptions(args: MutableList<String>, gameDir: File, assetsDir: File, profile: String) {
        hyperiumArguments.addAll(
            args + listOf(
                "--gameDir", gameDir.absolutePath,
                "--assetsDir", assetsDir.absolutePath,
                "--version", profile
            )
        )
    }

    override fun getLaunchTarget() = "net.minecraft.client.main.Main"

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        Hyperium.LOGGER.info("Initialising patcher...")
        try {
            PatchManager.INSTANCE.setup(classLoader, false)
        } catch (e: IOException) {
            throw RuntimeException(e)
            // we want to crash because users will bug us if we just return so just throw it
        }

        classLoader.registerTransformer("cc.hyperium.launch.patching.PatchTransformer")
        try {
            if (Launch.classLoader.getClassBytes("ave") != null) {
                // we are in obfuscated environment. nobody wants obfuscation. begone
                classLoader.registerTransformer("cc.hyperium.launch.deobf.DeobfTransformer")
            }
        } catch (ignored: IOException) {
        }

        Hyperium.LOGGER.info("Loading Addons...")
        Hyperium.LOGGER.info("Initialising Bootstraps...")
        MixinBootstrap.init()
        init()
        Hyperium.LOGGER.info("Applying transformers...")
        val environment = MixinEnvironment.getDefaultEnvironment()

        if (runningOptifine) {
            environment.obfuscationContext = "notch"
        }

        if (environment.obfuscationContext == null) {
            environment.obfuscationContext = "notch"
        }

        try {
            classLoader.addURL(
                File(
                    System.getProperty("java.home"),
                    "lib/ext/nashorn.jar"
                ).toURI().toURL()
            )
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        environment.side = MixinEnvironment.Side.CLIENT
    }

    override fun getLaunchArguments(): Array<String?> {
        return if (runningOptifine) arrayOfNulls(0)
        else hyperiumArguments.toTypedArray()
    }
}
