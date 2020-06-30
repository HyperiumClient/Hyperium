package cc.hyperium.launch.deobf.mappings

import cc.hyperium.Hyperium
import cc.hyperium.utils.sha256
import club.ampthedev.mcgradle.Properties
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.security.MessageDigest
import java.util.zip.ZipFile

/**
 * Object containing most used mappings
 */
object Mappings {
    /**
     * Mappings for MCP version stable_22
     */
    val stable22 = Mappings::class.java.getResourceAsStream("/mappings/joined.srg").use { load(it) }

    /**
     * Loads an SRG file and MCP data into a [MappingContainer].
     */
    private fun load(srg: InputStream): MappingContainer {
        // Download MCP data if necessary
        val dataDir = File("mappings").apply { mkdirs() }
        val mcpDataZip = File(dataDir, "mcp-data.zip")
        var redownloaded = false
        var tries = 0
        while (!mcpDataZip.exists() || mcpDataZip.sha256 != "aeed0aaba9d159b7ce60a21e2dcc36adb249fade65ce2f76c730dd0ec7270763") {
            redownloaded = true
            if (tries++ >= 10) {
                error("Failed to download MCP data")
            }
            Hyperium.LOGGER.info("Downloading MCP data (try $tries/10)")
            "https://files.minecraftforge.net/maven/de/oceanlabs/mcp/mcp_stable/22-1.8.9/mcp_stable-22-1.8.9.zip".downloadTo(mcpDataZip)
        }
        // Unzip MCP data if necessary
        val mcpDataDir = File(dataDir, "mcp-data")
        if (redownloaded || !mcpDataDir.exists()) {
            mcpDataDir.deleteRecursively()
            mcpDataZip.unzipTo(mcpDataDir)
        }
        // Load and process CSV and SRG data
        val fieldsCsv = File(mcpDataDir, "fields.csv").mcpCsvData
        val methodsCsv = File(mcpDataDir, "methods.csv").mcpCsvData
        val srgAsString = srg.bufferedReader().readText()
        val classes = hashMapOf<String, MappingContainer.ClassData>()

        srgAsString.lines().forEach { line ->
            val parts = line.split(" ")
            when (parts[0]) {
                "CL:" -> classes.computeIfAbsent(parts[1]) { MappingContainer.ClassData(parts[1], parts[2]) }
                "FD:" -> {
                    val (obfOwner, obfName) = parts[1].run { lastIndexOf('/').let { substring(0, it) to substring(it + 1) } }
                    val (deobfOwner, deobfName) = parts[2].run { lastIndexOf('/').let { substring(0, it) to substring(it + 1) } }
                    classes.getOrPut(obfOwner) { MappingContainer.ClassData(obfOwner, deobfOwner) }.fields[obfName] = fieldsCsv[deobfName]
                            ?: deobfName
                }
                "MD:" -> {
                    val (obfOwner, obfName) = parts[1].run { lastIndexOf('/').let { substring(0, it) to substring(it + 1) } }
                    val obfDesc = parts[2]
                    val (deobfOwner, deobfName) = parts[3].run { lastIndexOf('/').let { substring(0, it) to substring(it + 1) } }
                    classes.getOrPut(obfOwner) { MappingContainer.ClassData(obfOwner, deobfOwner) }.methods[obfName + obfDesc] = methodsCsv[deobfName]
                            ?: deobfName
                }
            }
        }

        return MappingContainer(classes)
    }

    /**
     * Parses an MCP CSV file and returns a map from SRG to MCP names.
     */
    private val File.mcpCsvData
        get() = bufferedReader().use { reader ->
            var line = reader.readLine()
            val map = hashMapOf<String, String>()
            while (line != null) {
                line = reader.readLine()
                line?.split(",")?.let { map[it[0]] = it[1] }
            }
            map
        }

    /**
     * Downloads a URL to a file.
     */
    private fun String.downloadTo(file: File) {
        URL(this).openConnection().apply {
            setRequestProperty("User-Agent", "Hyperium/${Properties.hyperiumVersion}")
            getInputStream().use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }

    /**
     * Unzips a file to a directory.
     */
    private fun File.unzipTo(directory: File) {
        directory.mkdirs()
        ZipFile(this).use { file ->
            file.entries().iterator().forEach {
                val destFile = File(directory, it.name.removePrefix("/"))
                destFile.parentFile?.mkdirs()
                file.getInputStream(it).use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}