package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.CTJS
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.awt.Color
import java.io.File
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

object Config {
    @ConfigOpt(name = "Directory", x = -110, y = 10, type = ConfigString::class)
    var modulesFolder: String = "./" + CTJS.configLocation.toRelativeString(File("./")) + "/modules"

    @ConfigOpt(name = "Print Chat To Console", x = -110, y = 65, type = ConfigBoolean::class)
    var printChatToConsole: Boolean = true

    @ConfigOpt(name = "Show Update Messages in Chat", x = -110, y = 120, type = ConfigBoolean::class)
    var showUpdatesInChat: Boolean = true

    @ConfigOpt(name = "Update Modules on Launch", x = -110, y = 175, type = ConfigBoolean::class)
    var updateModulesOnBoot: Boolean = true

    @ConfigOpt(name = "Clear Console on Load", x = 110, y = 10, type = ConfigBoolean::class)
    var clearConsoleOnLoad: Boolean = true

    @ConfigOpt(name = "Auto-Open Console on Error", x = 110, y = 65, type = ConfigBoolean::class)
    var openConsoleOnError: Boolean = false

    @ConfigOpt(name = "Custom Console Theme", x = 110, y = 120, type = ConfigBoolean::class)
    var customTheme: Boolean = false

    @ConfigOpt(name = "Console Theme", x = 110, y = 175, type = ConsoleThemeSelector::class)
    var consoleTheme: String = "default.dark"

    @ConfigOpt(name = "Console Foreground Color", x = 110, y = 175, type = SpecialConfigColor::class)
    var consoleForegroundColor: Color = Color(208, 208, 208)

    @ConfigOpt(name = "Console Background Color", x = 110, y = 250, type = SpecialConfigColor::class)
    var consoleBackgroundColor: Color = Color(21, 21, 21)

    fun load(jsonObject: JsonObject) {
        val gson = Gson()

        this::class.declaredMemberProperties.filter {
            it.annotations.any { ann ->
                ann.annotationClass == ConfigOpt::class
            } && it is KMutableProperty<*>
        }.map {
            it as KMutableProperty<*>
        }.forEach {
            val wrapper = jsonObject.getAsJsonObject(it.name)

            val value = gson.fromJson(wrapper.get("value"), it.javaField?.type)

            it.setter.call(this, value)
        }
    }

    fun save(file: File) {
        val obj = JsonObject()
        val gson = GsonBuilder().setPrettyPrinting().create()

        this::class.declaredMemberProperties.filter {
            it.annotations.any { ann ->
                ann.annotationClass == ConfigOpt::class
            }
        }.forEach {
            val wrapper = JsonObject()
            wrapper.add("value", gson.toJsonTree(it.getter.call(this)))

            obj.add(it.name, wrapper)
        }

        file.writeText(
            gson.toJson(obj)
        )
    }
}
