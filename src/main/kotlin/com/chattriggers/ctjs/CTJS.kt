package com.chattriggers.ctjs

import cc.hyperium.Hyperium
import cc.hyperium.event.EventBus
import cc.hyperium.event.InitializationEvent
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.PreInitializationEvent
import cc.hyperium.mixins.renderer.IMixinRenderLivingEntity
import cc.hyperium.mixinsimp.renderer.IMixinRenderManager
import cc.hyperium.mods.sk1ercommon.Multithreading
import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.UriScheme
import com.chattriggers.ctjs.utils.capes.LayerCape
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.kotlin.AnnotationHandler
import com.google.gson.JsonParser
import org.apache.commons.codec.digest.DigestUtils
import org.reflections.Reflections
import java.io.File
import java.io.FileReader
import kotlin.concurrent.thread

object CTJS {
    lateinit var assetsDir: File
    lateinit var configLocation: File
    val sounds = mutableListOf<Sound>()
    val reflections: Reflections

    init {
        EventBus.INSTANCE.register(this)

        reflections = Reflections("com.chattriggers.ctjs")
    }

    @InvokeEvent
    fun preInit(event: PreInitializationEvent) {
        this.configLocation = File(Hyperium.folder, "ctjs")
        configLocation.mkdir()

        val pictures = File(configLocation, "images/")
        pictures.mkdirs()
        assetsDir = pictures

        Multithreading.runAsync(thread {   loadConfig() })

        AnnotationHandler.subscribeAutomatic()

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()
    }

    @InvokeEvent
    fun init(event: InitializationEvent) {
        Multithreading.runAsync(thread { ModuleManager.load(true) })

        registerHooks()

        Multithreading.runAsync(thread {
            val sha256uuid = DigestUtils.sha256Hex(Player.getUUID())
            FileLib.getUrlContent("https://www.chattriggers.com/tracker/?uuid=$sha256uuid")
        })

        (Client.getMinecraft().renderManager as IMixinRenderManager).skinMap.values.forEach {
            (it as IMixinRenderLivingEntity<*>).callAddLayer(LayerCape(it))
        }
    }

    fun saveConfig() {
        val file = File(this.configLocation, "ChatTriggers.json")
        Config.save(file)
    }

    internal fun loadConfig(): Boolean {
        try {
            val parser = JsonParser()
            val obj = parser.parse(
                    FileReader(
                            File(this.configLocation, "ChatTriggers.json")
                    )
            ).asJsonObject

            Config.load(obj)

            return true
        } catch (exception: Exception) {
            val place = File(this.configLocation, "ChatTriggers.json")
            place.delete()
            place.createNewFile()
            saveConfig()
        }

        return false
    }

    private fun registerHooks() {
        Hyperium.INSTANCE.handlers.hyperiumCommandHandler.registerCommand(CTCommand)

        Runtime.getRuntime().addShutdownHook(
                Thread { TriggerType.GAME_UNLOAD::triggerAll }
        )
    }

    @JvmStatic
    fun loadIntoJVM() {
        
    }
}
