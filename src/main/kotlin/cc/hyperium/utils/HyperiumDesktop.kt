package cc.hyperium.utils

import cc.hyperium.Hyperium
import org.apache.commons.lang3.SystemUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI

object HyperiumDesktop {
    fun browse(uri: URI): Boolean {
        if (browseDesktop(uri) || openSystemSpecific(uri.toString())) {
            return true
        }

        Hyperium.LOGGER.warn("Failed to browse $uri")
        return false
    }

    fun open(file: File): Boolean {
        if (openDesktop(file) || openSystemSpecific(file.path)) {
            return true
        }

        Hyperium.LOGGER.warn("Failed to open ${file.absolutePath}")
        return false
    }

    fun edit(file: File): Boolean {
        if (editDesktop(file) || openSystemSpecific(file.path)) {
            return true
        }

        Hyperium.LOGGER.warn("Failed to edit ${file.absolutePath}")
        return false
    }


    private fun openSystemSpecific(file: String): Boolean {
        return when {
            SystemUtils.IS_OS_LINUX -> {
                return when {
                    isXdg -> runCommand("xdg-open", "%s", file)
                    isKde -> runCommand("kde-open", "%s", file)
                    isGnome -> runCommand("gnome-open", "%s", file)
                    else -> runCommand("kde-open", "%s", file) || runCommand("gnome-open", "%s", file)
                }
            }
            SystemUtils.IS_OS_MAC -> runCommand("open", "%s", file)
            SystemUtils.IS_OS_WINDOWS -> runCommand("explorer", "%s", file)
            else -> false
        }
    }


    private fun browseDesktop(uri: URI): Boolean {
        return try {
            if (!Desktop.isDesktopSupported()) {
                Hyperium.LOGGER.debug("Desktop is not supported. OS: " + System.getProperty("os.name"))
                return false
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Hyperium.LOGGER.debug("BROWSE is not supported.")
                return false
            }

            Hyperium.LOGGER.debug("Attempting to open $uri with Desktop#browse()")
            Desktop.getDesktop().browse(uri)
            true
        } catch (t: Throwable) {
            Hyperium.LOGGER.error("Error using desktop browse.", t)
            false
        }
    }

    private fun openDesktop(file: File): Boolean {
        return try {
            if (!Desktop.isDesktopSupported()) {
                Hyperium.LOGGER.debug("Desktop is not supported. OS: " + System.getProperty("os.name"))
                return false
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Hyperium.LOGGER.warn("OPEN is not supported.")
                return false
            }

            Hyperium.LOGGER.debug("Attempting to open $file with Desktop#open()")
            Desktop.getDesktop().open(file)
            true
        } catch (t: Throwable) {
            Hyperium.LOGGER.error("Error using desktop open.", t)
            false
        }
    }

    private fun editDesktop(file: File): Boolean {
        return try {
            if (!Desktop.isDesktopSupported()) {
                Hyperium.LOGGER.debug("Desktop is not supported. OS: " + System.getProperty("os.name"))
                return false
            }
            if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
                Hyperium.LOGGER.debug("EDIT is not supported.")
                return false
            }
            Hyperium.LOGGER.debug("Attempting to edit $file with Desktop#edit()")
            Desktop.getDesktop().edit(file)
            true
        } catch (t: Throwable) {
            Hyperium.LOGGER.error("Error using desktop edit.", t)
            false
        }
    }

    @Suppress("SameParameterValue")
    private fun runCommand(command: String, args: String, file: String): Boolean {
        Hyperium.LOGGER.debug("Trying to exec:\n   cmd = $command\n   args = $args\n   %s = $file")
        val parts = prepareCommand(command, args, file).joinToString(" ")
        return try {
            val p = Runtime.getRuntime().exec(parts) ?: return false
            try {
                Hyperium.LOGGER.error(if (p.exitValue() == 0) "Process ended immediately." else "Process crashed.")
                false
            } catch (_: IllegalThreadStateException) {
                Hyperium.LOGGER.error("Process is running.")
                true
            }
        } catch (e: IOException) {
            Hyperium.LOGGER.error("Error running command.", e)
            false
        }
    }

    private fun prepareCommand(command: String, args: String, file: String) =
        listOf(command) + args.split(" ").map { it.format(file).trim() }

    private val isXdg: Boolean
        get() = System.getenv("XDG_SESSION_ID")?.isNotEmpty() == true

    private val isGnome: Boolean
        get() = System.getenv("GDMSESSION")?.toLowerCase()?.contains("gnome") == true

    private val isKde: Boolean
        get() = System.getenv("GDMSESSION")?.toLowerCase()?.contains("kde") == true
}