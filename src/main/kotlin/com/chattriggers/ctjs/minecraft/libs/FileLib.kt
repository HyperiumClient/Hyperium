package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.utils.config.Config
import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URL
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileLib {
    /**
     * Writes a file to folder in modules.
     *
     * @param importName name of the import
     * @param fileName   name of the file
     * @param toWrite    string to write in file
     */
    @JvmStatic
    fun write(importName: String, fileName: String, toWrite: String) =
        write(Config.modulesFolder + "/" + importName + "/" + fileName, toWrite)

    /**
     * Writes a file to anywhere on the system.<br></br>
     * Use "./" for the ".minecraft" folder.
     *
     * @param fileLocation the location and file name
     * @param toWrite      string to write in file
     */
    @JvmStatic
    fun write(fileLocation: String, toWrite: String) {
        FileUtils.write(File(fileLocation), toWrite, Charset.defaultCharset())
    }

    /**
     * Reads a file from folder in modules.<br></br>
     * Returns an empty string if file is not found.
     *
     * @param importName name of the import
     * @param fileName   name of the file
     * @return the string in the file
     */
    @JvmStatic
    fun read(importName: String, fileName: String) =
        read(Config.modulesFolder + "/" + importName + "/" + fileName)

    /**
     * Reads a file from anywhere on the system.<br></br>
     * Use "./" for the ".minecraft" folder.<br></br>
     * Returns an empty string if file is not found.
     *
     * @param fileLocation the location and file name
     * @return the string in the file
     */
    @JvmStatic
    fun read(fileLocation: String): String {
        val file = File(fileLocation)
        return read(file)
    }

    /**
     * Reads a file from anywhere on the system using java.io.File.
     *
     * @param file the java.io.File to load
     * @return the string in the file
     */
    @JvmStatic
    fun read(file: File): String {
        return FileUtils.readFileToString(file, Charset.defaultCharset())
    }

    /**
     * Gets the contents of a url as a string.
     *
     * @param theUrl the url to get the data from
     * @param userAgent the user agent to use in the connection
     * @return the string stored in the url content
     */
    @JvmStatic @JvmOverloads
    fun getUrlContent(theUrl: String, userAgent: String? = null): String {
        return URL(theUrl).readText()
    }

    @JvmStatic
    fun deleteDirectory(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.listFiles()
            for (child in children!!) {
                if (!deleteDirectory(child)) return false
            }
        }

        return dir.delete()
    }

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists).
     * @param zipFilePath the zip file path
     * @param destDirectory the destination directory
     * @throws IOException IOException
     */
    @JvmStatic
    @Throws(IOException::class)
    fun unzip(zipFilePath: String, destDirectory: String) {
        val destDir = File(destDirectory)
        if (!destDir.exists()) destDir.mkdir()

        val zipIn = ZipInputStream(FileInputStream(zipFilePath))
        var entry: ZipEntry? = zipIn.nextEntry
        // iterates over entries in the zip file
        while (entry != null) {
            val filePath = destDirectory + File.separator + entry.name
            if (!entry.isDirectory) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath)
            } else {
                // if the entry is a directory, make the directory
                val dir = File(filePath)
                dir.mkdir()
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }

    // helper method for unzipping
    @Throws(IOException::class)
    private fun extractFile(zipIn: ZipInputStream, filePath: String) {
        val toWrite = File(filePath)
        toWrite.parentFile.mkdirs()
        toWrite.createNewFile()

        val bos = BufferedOutputStream(FileOutputStream(filePath))
        val bytesIn = ByteArray(4096)
        var read = zipIn.read(bytesIn)
        while (read != -1) {
            bos.write(bytesIn, 0, read)
            read = zipIn.read(bytesIn)
        }
        bos.close()
    }
}