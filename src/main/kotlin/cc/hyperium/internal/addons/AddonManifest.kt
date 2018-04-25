package cc.hyperium.internal.addons

/**
 * Instance created on the classloader net.minecraft.launchwrapper.LaunchClassLoader
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class AddonManifest {

    /**
     * The Identifier the addon goes by.
     * <i>This should be unique</i>
     * @return addon name
     */
    var name: String? = null

    /**
     * Version of the addon
     * <i>Preferred versioning number pattern is <b>MAJOR.MINOR.PATCH</b></i>
     * MAJOR - version when you make incompatible API changes.
     * MINOR - version when you add functionality in a backwards-compatible manner.
     * PATCH - version when you make backwards-compatible bug fixes.
     *
     * @return addon version
     */
    var version: String? = null

    /**
     * Main Class of an addon
     * e.g.com.example.ExampleAddon
     *
     * @return main-class
     */
    val mainClass: String? = null

    /**
     * If the addon includes and Mixins you
     * can add the config(s) here which will
     * automatically at them to the environment
     *
     * @return mixin configs
     */
    val mixinConfigs: List<String>? = null

    /**
     * If the addon includes a <i>tweaker</i>
     * you can specify it here and it will
     * add it to the launchwrapper.
     * <b>IT MUST IMPLEMENT {@link net.minecraft.launchwrapper.ITweaker}!</b>
     *
     * @return tweaker-class
     */
    val tweakerClass: String? = null

    /**
     * An array containing all the names of
     * the dependencies. The dependencies
     * should be loaded before this addon.
     *
     * @return the names of the dependencies
     */
    val dependencies: List<String> = ArrayList()
}