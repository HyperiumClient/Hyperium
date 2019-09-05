package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.console.Console
import org.apache.commons.io.FileUtils
import java.io.File

interface ILoader {
    val console: Console
    fun load(modules: List<Module>)
    fun load(module: Module)
    fun exec(type: TriggerType, vararg args: Any?)
    fun eval(code: String): Any?
    fun addTrigger(trigger: OnTrigger)
    fun clearTriggers()
    fun getLanguageName(): String
    fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?)
    fun removeTrigger(trigger: OnTrigger)
    fun getModules(): List<Module>

    companion object {
        internal val modulesFolder = File(Config.modulesFolder)

        init {
            modulesFolder.mkdir()
        }

        internal fun getFoldersInDir(dir: File): List<File> {
            if (!dir.isDirectory) return emptyList()

            return dir.listFiles().filter {
                it.isDirectory
            }
        }

        /**
         * Save a resource to the OS's filesystem from inside the jar
         * @param resourceName name of the file inside the jar
         * @param outputFile file to save to
         * @param replace whether or not to replace the file being saved to
         */
        fun saveResource(resourceName: String?, outputFile: File, replace: Boolean): String {
            if (resourceName == null || resourceName == "") {
                throw IllegalArgumentException("ResourcePath cannot be null or empty")
            }

            val parsedResourceName = resourceName.replace('\\', '/')
            val resource = this::class.java.getResourceAsStream(parsedResourceName)
                ?: throw IllegalArgumentException("The embedded resource '$parsedResourceName' cannot be found.")

            val res = resource.bufferedReader().readText()
            FileUtils.write(outputFile, res)
            return res
        }
    }
}
