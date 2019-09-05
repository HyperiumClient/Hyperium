package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.ILoader.Companion.saveResource
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.ModuleLoader
import jdk.nashorn.api.scripting.NashornScriptEngine
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.objects.Global
import net.minecraft.client.Minecraft
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ModuleLoader
object JSLoader : ILoader {
    private val triggers = mutableListOf<OnTrigger>()
    private val toRemove = mutableListOf<OnTrigger>()
    override val console by lazy { Console(this) }

    private var global: Global? = null
    private val cachedModules = mutableListOf<Module>()
    private lateinit var scriptEngine: NashornScriptEngine

    override fun load(modules: List<Module>) {
        cachedModules.clear()

        val jars = modules.map {
            it.folder.listFiles().toList()
        }.flatten().filter {
            it.name.endsWith(".jar")
        }.map {
            it.toURI().toURL()
        }

        scriptEngine = instanceScriptEngine(jars)

        val script = saveResource(
            "/providedLibs.js",
            File(
                modulesFolder.parentFile,
                "chattriggers-provided-libs.js"
            ),
            true
        )

        try {
            scriptEngine.eval(script)
        } catch (e: Exception) {
            console.printStackTrace(e)
        }

        val combinedScript = modules.map {
            it.getFilesWithExtension(".js")
        }.flatten().joinToString(separator = "\n") {
            it.readText()
        }

        try {
            scriptEngine.eval(combinedScript)
        } catch (e: Exception) {
            console.printStackTrace(e)
        }

        cachedModules.addAll(modules)
    }

    override fun load(module: Module) {
        if (cachedModules.map {
                it.name
            }.contains(module.name)) return

        cachedModules.add(module)

        val script = module.getFilesWithExtension(".js").joinToString(separator = "\n") {
            it.readText()
        }

        try {
            scriptEngine.eval(script)
        } catch (e: Exception) {
            console.out.println("Error loading module ${module.name}")
            console.printStackTrace(e)
        }
    }

    override fun exec(type: TriggerType, vararg args: Any?) {
        triggers.removeAll(toRemove)
        toRemove.clear()

        try {
            triggers.filter {
                it.type == type
            }.forEach {
                try {
                    it.trigger(*args)
                } catch (e: Exception) {
                    console.printStackTrace(e)
                    removeTrigger(it)
                }
            }
        } catch (e: Exception) {
            console.printStackTrace(e)
        }
    }

    override fun eval(code: String): Any? = scriptEngine.eval(code)

    override fun addTrigger(trigger: OnTrigger) {
        triggers.add(trigger)

        triggers.sortBy {
            it.priority.ordinal
        }
    }

    override fun clearTriggers() {
        triggers.clear()
    }

    override fun getLanguageName(): String = "js"

    override fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?) {
        try {
            if (method is String) {
                callNamedMethod(method, *args)
            } else {
                callActualMethod(method, *args)
            }
        } catch (e: Exception) {
            console.printStackTrace(e)
            removeTrigger(trigger)
        }
    }

    override fun removeTrigger(trigger: OnTrigger) {
        toRemove.add(trigger)
    }

    override fun getModules(): List<Module> = cachedModules

    private fun callActualMethod(method: Any, vararg args: Any?) {
        val som: ScriptObjectMirror = if (method is ScriptObjectMirror) {
            method
        } else {
            if (global == null) {
                val prop = NashornScriptEngine::class.memberProperties.firstOrNull {
                    it.name == "global"
                }!!

                prop.isAccessible = true
                global = prop.get(scriptEngine) as Global
            }

            val obj = ScriptObjectMirror.wrap(method, global)

            obj as ScriptObjectMirror
        }

        som.call(som, *args)
    }

    private fun callNamedMethod(method: String, vararg args: Any?) {
        scriptEngine.invokeFunction(method, *args)
    }

    private fun instanceScriptEngine(files: List<URL>): NashornScriptEngine {
        val ucl = URLClassLoader(files.toTypedArray(), Minecraft::class.java.classLoader)

        return NashornScriptEngineFactory().getScriptEngine(ucl) as NashornScriptEngine
    }
}
