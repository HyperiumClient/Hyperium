package cc.hyperium.launch

import cc.hyperium.Hyperium
import cc.hyperium.launch.patching.PatchManager
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import java.io.File
import java.io.IOException
import java.net.MalformedURLException

class HyperiumTweaker : ITweaker {

    private val hyperiumArguments = arrayListOf<String>()

    // new
    val optifine = Launch.classLoader.transformers.any {
        it.javaClass.name == "optifine.OptiFineForgeTweaker"
    }

    override fun acceptOptions(args: MutableList<String>, gameDir: File, assetsDir: File, profile: String) {
        hyperiumArguments += args + arrayListOf(
            "--gameDir", gameDir.absolutePath,
            "--assetsDir", assetsDir.absolutePath,
            "--version", profile
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

        try {
            val clazz = Class.forName("cc.hyperium.ClassLoaderHelper", false, classLoader)
            val m = clazz.getMethod("injectIntoClassLoader", Boolean::class.java)
            m.invoke(null, optifine)
        } catch (e: Exception) {
            e.printStackTrace()
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
    }

    override fun getLaunchArguments(): Array<String?> {
        return if (optifine) arrayOfNulls(0)
        else hyperiumArguments.toTypedArray()
    }
}