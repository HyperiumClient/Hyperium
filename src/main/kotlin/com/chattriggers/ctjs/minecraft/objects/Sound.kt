package com.chattriggers.ctjs.minecraft.objects

import jdk.nashorn.api.scripting.ScriptObjectMirror
import java.net.MalformedURLException

/**
 * <p>
 *     Instances a new Sound with certain properties. These properties
 *     should be passed through as a normal JavaScript object.
 * </p>
 *
 * <p>
 *     REQUIRED:<br>
 *     &emsp;source (String) - filename, relative to ChatTriggers assets directory
 * </p>
 *
 * <p>
 *     OPTIONAL:<br>
 *     &emsp;priority (boolean) - whether or not this sound should be prioritized, defaults to false<br>
 *     &emsp;loop (boolean) - whether or not to loop this sound over and over, defaults to false<br>
 *     &emsp;stream (boolean) - whether or not to stream this sound rather than preload it (should be true for large files), defaults to false
 * </p>
 *
 * <p>
 *     CONFIGURABLE (can be set in config object, or changed later, but MAKE SURE THE WORLD HAS LOADED)<br>
 *     &emsp;category (String) - which category this sound should be a part of, see {@link #setCategory(String)}.<br>
 *     &emsp;volume (float) - volume of the sound, see {@link #setVolume(float)}<br>
 *     &emsp;pitch (float) - pitch of the sound, see {@link #setPitch(float)}<br>
 *     &emsp;x, y, z (float) - location of the sound, see {@link #setPosition(float, float, float)}. Defaults to the players position.<br>
 *     &emsp;attenuation (int) - fade out model of the sound, see {@link #setAttenuation(int)}<br>
 * </p>
 *
 * @param config the JavaScript config object
 */
class Sound(private val config: ScriptObjectMirror) {
    /*private var sndSystem: SoundSystem? = null
    private val source: String = config["source"] as String*/
    var isListening = false

    init {
        /*if (World.isLoaded()) {
            loadSndSystem()

            try {
                bootstrap()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

        } else {
            isListening = true
        }

        CTJS.sounds.add(this)*/
    }

    fun onWorldLoad() {
        /*isListening = false

        println("Loading sound system")
        loadSndSystem()
        println("sound system loaded: " + sndSystem!!)

        try {
            println("Bootstrapping")
            bootstrap()
        } catch (exc: MalformedURLException) {
            exc.printStackTrace()
        }
*/
    }

    private fun loadSndSystem() {
        /*val sndManager = (Client.getMinecraft().soundHandler as MixinSoundHandler).sndManager

        sndSystem = (sndManager as IMixinSoundManager).soundSystem*/
    }

    @Throws(MalformedURLException::class)
    private fun bootstrap() {
        /*val configMap = config as? Map<String, *> ?: return

        val source = config["source"]?.toString()
        val priority = configMap.getOrDefault("priority", false) as Boolean
        val loop = configMap.getOrDefault("loop", false) as Boolean
        val stream = configMap.getOrDefault("stream", false) as Boolean

        val url = File("${CTJS.assetsDir.absolutePath}\\$source").toURI().toURL()
        val x = configMap.getOrDefault("x", Player.getX()) as Float
        val y = configMap.getOrDefault("y", Player.getY()) as Float
        val z = configMap.getOrDefault("z", Player.getZ()) as Float
        val attModel = configMap.getOrDefault("attenuation", 1) as Int
        val distOrRoll = 16

        if (stream) {
            sndSystem!!.newStreamingSource(
                priority,
                source,
                url,
                source,
                loop,
                x,
                y,
                z,
                attModel,
                distOrRoll.toFloat()
            )
        } else {
            sndSystem!!.newSource(
                priority,
                source,
                url,
                source,
                loop,
                x,
                y,
                z,
                attModel,
                distOrRoll.toFloat()
            )
        }

        if (config.hasMember("volume")) {
            setVolume(config["volume"] as Float)
        }

        if (config.hasMember("pitch")) {
            setPitch(config["pitch"] as Float)
        }

        if (config.hasMember("category")) {
            setCategory(config["category"] as String)
        }*/
    }

    /**
     * Sets the category of this sound, making it respect the Player's sound volume sliders.
     * Options are: MASTER, MUSIC, RECORDS, WEATHER, BLOCKS, MOBS, ANIMALS, PLAYERS, and AMBIENT
     *
     * @param category the category
     */
    fun setCategory(category: String) = apply {
        /*val category1 = SoundCategory.valueOf(category.toUpperCase())
        setVolume(Client.getMinecraft().gameSettings.getSoundLevel(category1))*/
    }

    /**
     * Sets this sound's volume.
     * Will override the category if called after [.setCategory], but not if called before.
     *
     * @param volume New volume, float value ( 0.0f - 1.0f ).
     */
    fun setVolume(volume: Float) = apply { /*sndSystem!!.setVolume(this.source, volume)*/ }

    /**
     * Updates the position of this sound
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    fun setPosition(x: Float, y: Float, z: Float) = apply { /*sndSystem!!.setPosition(this.source, x, y, z)*/ }

    /**
     * Sets this sound's pitch.
     *
     * @param pitch A float value ( 0.5f - 2.0f ).
     */
    fun setPitch(pitch: Float) = apply { /*sndSystem!!.setPitch(this.source, pitch)*/ }

    /**
     * Sets the attenuation (fade out over space) of the song.
     * Models are:
     *  NONE(0) - no fade
     *  ROLLOFF(1) - this is the default, meant to be somewhat realistic
     *  LINEAR(2) - fades out linearly, as the name implies
     *
     * @param model the model
     */
    fun setAttenuation(model: Int) = apply { /*sndSystem!!.setAttenuation(this.source, model)*/ }

    /**
     * Plays/resumes the sound
     */
    fun play() { /*sndSystem!!.play(this.source)*/
    }

    /**
     * Pauses the sound, to be resumed later
     */
    fun pause() { /*sndSystem!!.pause(this.source)*/
    }

    /**
     * Completely stops the song
     */
    fun stop() { /*sndSystem!!.stop(this.source)*/
    }

    /**
     * I really don't know what this does
     */
    fun rewind() { /*sndSystem!!.rewind(this.source)*/
    }
}
