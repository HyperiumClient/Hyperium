package com.chattriggers.ctjs.utils

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ModuleManager
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.util.stream.Collectors

object UriScheme {
    private const val PORT = 21965
    private const val QUOTE = "\""

    fun installUriScheme() {
        try {
            regAdd(
                " /f /ve /d " + quote("URL:chattriggers Protocol")
            )

            regAdd(
                " /f /v " +
                        quote("URL Protocol") +
                        " /d " +
                        quote("")
            )

            val cp = CTJS.configLocation.absolutePath
            val sep = File.separator
            val javaProgram = System.getProperty("java.home") + sep + "bin" + sep + "javaw.exe"

            ILoader.saveResource(
                "/UriScheme.class",
                File("$cp${sep}com${sep}chattriggers${sep}ctjs${sep}loader${sep}UriScheme.class"),
                true
            )

            val value = """\"$javaProgram\" -cp \"$cp\" com.chattriggers.ctjs.loader.UriScheme \"%1\""""

            regAdd(
                ("\\shell\\open\\command /f /ve /d " +
                        "\"" + value + "\"")
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun createSocketListener() {
        Thread(Runnable { socketListener() }, "CTJSSocketListener").start()
    }

    private fun quote(toQuote: String): String = QUOTE + toQuote + QUOTE

    @Throws(IOException::class, InterruptedException::class)
    private fun regAdd(args: String) {
        val process = Runtime.getRuntime().exec("REG ADD HKCU\\Software\\Classes\\chattriggers$args")
        if (process.waitFor() != 0) {
            throw IOException("Error editing registry!")
        }
    }

    private fun socketListener() {
        try {
            ServerSocket(PORT).use { serverSocket ->
                while (!Thread.interrupted()) {
                    try {
                        serverSocket.accept().use { clientSocket ->
                            val inputStream = clientSocket.getInputStream()
                            val module = BufferedReader(InputStreamReader(inputStream))
                                .lines().collect(Collectors.joining("\n"))
                            ModuleManager.importModule(module)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
